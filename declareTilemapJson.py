def getTiles(height, width):
    values = [[[0 for d in range(3)] for w in range(int(width))] for h in range(int(height))]
    for y in range(int(height)):
        for x in range(int(width)):
            values[y][x][0] = input("Tile name?")
            values[y][x][1] = x
            values[y][x][2] = y
    return values

def formatMapLine(line):
    return r'\"' + str(line[0]) + r'\":{\"cornerX\":' + str(line[1]) + r',\"cornerY\":' + str(line[2]) + r'}'

def makeMap(values):
    jsonString = r'\"map\":{'
    for y in range(0, len(values)):
        for x in range(0, len(values[y])):
            jsonString = str(jsonString + formatMapLine(values[y][x]) + r',')

    return jsonString.strip(',') + r'}'

width = input("What's the width of the tilemap?")
height = input("What's the height of the tilemap?")

output = (makeMap(getTiles(height, width)))
print("\n" + output + "\n")
print(output.replace("\\", ""))