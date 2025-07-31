#!/bin/sh
set -e

BACKUP_DIR="/backup"
DATA_DIR="/var/lib/mysql"

# Automatically pick the latest backup
BACKUP_FILE=$(ls -t "$BACKUP_DIR"/mysql_data_backup_*.tar.gz 2>/dev/null | head -n 1)

if [ ! -f "$BACKUP_FILE" ]; then
  echo "No backup file found in $BACKUP_DIR."
  exit 1
fi

echo "Found backup: $BACKUP_FILE"
echo "Clearing existing MySQL data..."
rm -rf "$DATA_DIR"/*

echo "Restoring backup..."
tar -xzf "$BACKUP_FILE" -C "$DATA_DIR"

echo "Setting permissions..."
chown -R mysql:mysql "$DATA_DIR"

echo "Restore complete."
