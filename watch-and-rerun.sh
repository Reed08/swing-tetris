#!/usr/bin/env bash

# ─── CONFIGURE THESE ───────────────────────────────────────────────────────────
APP_PROC="java"                    # the process name System Events sees
WINDOW_TITLE="Tetris"  # your exact JFrame title in the title bar
# ────────────────────────────────────────────────────────────────────────────────

while fswatch -1 Main.java; do
  echo "🔄 Change detected – restarting…"

  # 1) Grab old window position, or "nil"
  oldpos=$(osascript <<EOF
tell application "System Events"
  if exists process "$APP_PROC" then
    try
      get position of window "$WINDOW_TITLE" of process "$APP_PROC"
    on error
      return "nil"
    end try
  else
    return "nil"
  end if
end tell
EOF
)

  echo "Old position: $oldpos"

  # 2) Kill any running "java Main"
  pkill -f 'java Main' 2>/dev/null || true

  # 3) Recompile & relaunch in background
  javac Main.java && java Main &

  # 4) If we have a valid oldpos, wait for the new GUI then reposition
  if [[ "$oldpos" != "nil" ]]; then
    echo "⏱ Waiting for \"$APP_PROC\" (with at least one window)…"

    # up to ~5s total, checking every 0.1s
    for i in {1..50}; do
      exists=$(osascript -e "tell application \"System Events\" to exists process \"$APP_PROC\"")
      if [[ "$exists" == "true" ]]; then
        win_count=$(osascript -e "tell application \"System Events\" to count windows of process \"$APP_PROC\"" 2>/dev/null || echo 0)
        if (( win_count > 0 )); then
          break
        fi
      fi
      sleep 0.1
    done

    # bring the app frontmost so System Events can see it clearly
    osascript -e "tell application \"System Events\" to set frontmost of process \"$APP_PROC\" to true" 2>/dev/null

    echo "📍 Repositioning \"$WINDOW_TITLE\" to {${oldpos}}"
    osascript <<EOF
tell application "System Events"
  tell process "$APP_PROC"
    try
      set position of window "$WINDOW_TITLE" to {${oldpos}}
    on error
      set position of window 1 to {${oldpos}}
    end try
  end tell
end tell
EOF
  fi
done