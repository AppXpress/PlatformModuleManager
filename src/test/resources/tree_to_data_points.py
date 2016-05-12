import re

f = open('mods_to_test', 'r')
datapoints = []
customer="_DEAD_"
module=""
for line in f:
  match = re.search(">", line)
  if match:
    module = line.split('>').pop().strip()
    datapoints.append((customer, module))
  else:
    customer=line.strip()
target = open("datapoints.txt", "w")
for point in datapoints:
  point_string = "@DataPoint public static String[] " + point[0] + "_" + point[1] + " = {\"" + point[0]+ "\", \"" + point[1] +  "\"};\n"
  target.write(point_string)

