import csv

input_file = 'backend/database/src/csv/parameters2.csv'
output_file = 'backend/database/src/csv/parameters3.csv'

with open(input_file, 'r', encoding='utf-8', newline='') as infile, \
     open(output_file, 'w', encoding='utf-8', newline='') as outfile:
    
    reader = csv.DictReader(infile)
    writer = csv.DictWriter(outfile, fieldnames=reader.fieldnames)
    
    writer.writeheader()
    
    for row in reader:
        for key in row:
            if row[key]:
                # Replace actual newlines with literal '\n'
                row[key] = row[key].replace('\r\n', '\\n').replace('\n', '\\n').replace('\r', '\\n').strip()
        writer.writerow(row)

print(f'Finished. Clean CSV with escaped newlines written to: {output_file}')
