#!/bin/sh
set -e

TIMESTAMP=$(date +'%Y%m%d_%H%M%S')
BACKUP_DIR="./local_backups"
BACKUP_FILE="mysql_data_backup_${TIMESTAMP}.sql"

# Ensure the backup directory exists
mkdir -p "$BACKUP_DIR"

echo "📦 Starting MySQL dump backup..."

# Run mysqldump inside the running MySQL container, outputting SQL dump to host backup dir
docker exec GeoTechnicalDatabase sh -c "mysqldump -u root -prootpassword --single-transaction --quick GeotechnicalTests" > "$BACKUP_DIR/$BACKUP_FILE"

# Check if backup file was created successfully
if [ -f "$BACKUP_DIR/$BACKUP_FILE" ]; then
  chmod 600 "$BACKUP_DIR/$BACKUP_FILE"
  echo "✅ Backup completed successfully: $BACKUP_FILE"
else
  echo "❌ Backup failed. No file created."
  exit 1
fi
