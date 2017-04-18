import json

with open('classes.json') as data_file:
	data = json.load(data_file)
	for l in data:
		print(l)
