#!/bin/sh

set -e

echo "Creating local backup of mysql_data volume..."

# Generate timestamped backup filename
TIMESTAMP=$(date +'%Y%m%d%H%M%S')
BACKUP_FILE="/backup/mysql_data_backup_$TIMESTAMP.tar.gz"

# Use tar directly in the restore container (no need to run docker-in-docker)
tar czf "$BACKUP_FILE" -C /data .

echo "Backup saved to $BACKUP_FILE"

# Optional: list backups
ls -lh /backup
