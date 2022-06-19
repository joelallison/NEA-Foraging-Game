# NEA-Forest-Game





# Documenting Terrain Generation Algorithm

<img src="https://user-images.githubusercontent.com/33546240/174500282-fd641eff-00d6-4fb4-8be4-873a7aa155d3.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 2f, 0.8f, 2, true <br /><br />
threshold >= 0.5 for (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />

<img src="https://user-images.githubusercontent.com/33546240/174500443-2f18e8a9-f3db-4465-86dd-af0b3d9b3719.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 2f, 0.4f, 2, true <br /><br />
noiseMap[x][y] >= 0.99 --> (0.7f, 0.65f, 0.9f, 1) colour <br />
noiseMap[x][y] >= 0.8 --> (0.5f, 0.5f, 0.1f, 1) colour <br />
wrapValue(noiseMap[x][y], -1, true) >= 0.95 --> (0.5f, 0.5f, 0.3f, 1f) colour <br />
wrapValue(noiseMap[x][y], 1, true) >= 0.9 --> (0.4f, 0.4f, 0.3f, 1) colour <br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />

<img src="https://user-images.githubusercontent.com/33546240/174500599-8148e28e-53db-4a59-911f-f6341f4ee08d.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 2f, 0.8f, -1, true <br /><br />
threshold >= 0.5 for (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />




