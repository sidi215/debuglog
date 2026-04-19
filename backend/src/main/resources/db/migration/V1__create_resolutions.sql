CREATE TABLE IF NOT EXISTS resolutions (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    error_hash  TEXT    NOT NULL,
    error_type  TEXT    NOT NULL,
    pattern     TEXT    NOT NULL,
    solution    TEXT    NOT NULL,
    confirmed   INTEGER NOT NULL DEFAULT 0,
    used_count  INTEGER NOT NULL DEFAULT 0,
    created_at  TEXT    NOT NULL DEFAULT (datetime('now'))
);

CREATE INDEX IF NOT EXISTS idx_resolutions_hash ON resolutions(error_hash);