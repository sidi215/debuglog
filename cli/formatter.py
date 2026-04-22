from rich.console import Console
from rich.panel import Panel

console = Console()

SEVERITY_COLORS = {
    "CRITICAL": "bold red",
    "HIGH": "red",
    "MEDIUM": "yellow",
    "LOW": "green",
}

def render_response(data: dict) -> None:
    severity = data.get("severity", "MEDIUM")
    color = SEVERITY_COLORS.get(severity, "yellow")

    console.print(Panel(
        f"[bold]{data.get('errorType', 'Unknown')}[/bold]\n"
        f"[dim]Format: {data.get('format', '?')} | "
        f"Severity: [{color}]{severity}[/{color}] | "
        f"{'[green]From KB[/green]' if data.get('fromKnowledgeBase') else '[blue]AI Analysis[/blue]'}[/dim]",
        title="[bold cyan]DebugLog AI[/bold cyan]",
        border_style="cyan"
    ))

    console.print(Panel(
        data.get("rootCause", ""),
        title="[yellow]Root Cause[/yellow]",
        border_style="yellow"
    ))

    console.print(Panel(
        data.get("solution", ""),
        title="[green]Solution[/green]",
        border_style="green"
    ))

def render_error(message: str) -> None:
    console.print(f"[bold red]Error:[/bold red] {message}")