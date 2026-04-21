#!/usr/bin/env python3
import sys
import os
import argparse
import requests
from formatter import render_response, render_error, console

BACKEND_URL = os.getenv("DEBUGLOG_URL", "http://localhost:8080")
ANALYZE_ENDPOINT = f"{BACKEND_URL}/api/v1/analyze"
CONFIRM_ENDPOINT = f"{BACKEND_URL}/api/v1/confirm"


def read_input(filepath: str | None) -> str:
    if filepath:
        with open(filepath, "r", encoding="utf-8") as f:
            return f.read()
    if not sys.stdin.isatty():
        return sys.stdin.read()
    render_error("Provide a log file or pipe input. Usage: debuglog error.log")
    sys.exit(1)


def analyze(log_content: str) -> dict:
    try:
        response = requests.post(
            ANALYZE_ENDPOINT,
            json={"logContent": log_content},
            timeout=120
        )
        response.raise_for_status()
        return response.json()
    except requests.exceptions.ConnectionError:
        render_error("Cannot connect to DebugLog backend. Is it running?")
        sys.exit(1)
    except requests.exceptions.Timeout:
        render_error("Request timed out. The model may be loading, try again.")
        sys.exit(1)
    except requests.exceptions.HTTPError as e:
        error_msg = e.response.json().get("error", str(e))
        render_error(error_msg)
        sys.exit(1)


def prompt_confirmation(session_id: str) -> None:
    try:
        answer = console.input("\n[dim]Did this solve your issue? [y/N]:[/dim] ").strip().lower()
        if answer == "y":
            requests.post(f"{CONFIRM_ENDPOINT}/{session_id}", timeout=10)
            console.print("[green]Solution saved to knowledge base.[/green]")
    except (KeyboardInterrupt, EOFError):
        pass


def main() -> None:
    parser = argparse.ArgumentParser(
        prog="debuglog",
        description="DebugLog — local AI log analyzer"
    )
    parser.add_argument("file", nargs="?", help="Log file to analyze")
    args = parser.parse_args()

    log_content = read_input(args.file)
    result = analyze(log_content)
    render_response(result)
    prompt_confirmation(result.get("sessionId", ""))


if __name__ == "__main__":
    main()