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

<img src="https://user-images.githubusercontent.com/33546240/174500707-739167aa-de0c-4754-8b39-74489e3ec54e.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 2f, 0.4f, -1, true <br /><br />
noiseMap[x][y] >= 0.8 --> (0.5f, 0.5f, 0.3f, 1f) colour <br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />

<img src="https://user-images.githubusercontent.com/33546240/174500906-ce7f44b6-6527-4a8f-873c-917f37840b62.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 2f, 0.4f, -1, true <br /><br />
noiseMap[x][y] >= 0.96 --> (0.7f, 0.65f, 0.9f, 1) colour <br />
noiseMap[x][y] >= 0.8 --> (0.5f, 0.5f, 0.1f, 1) colour <br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />

<img src="https://user-images.githubusercontent.com/33546240/174501233-c7c8e5af-3769-4828-99c2-e9167cab911e.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 2f, 0.5f, -1, true <br /><br />
noiseMap[x][y] >= 0.96 --> (0.7f, 0.65f, 0.9f, 1) colour <br />
noiseMap[x][y] >= 0.62 --> //commented out so it does weird glitchy effect// colour <br />
noiseMap[x][y] >= 0.6 --> (0.5f, 0.5f, 0.3f, 1f) colour <br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />

<img src="https://user-images.githubusercontent.com/33546240/174501194-99dd9a23-166a-4f66-8420-303c5755c604.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 4, 2f, 0.5f, 4, true <br /><br />
noiseMap[x][y] >= 0.96 --> (0.7f, 0.65f, 0.9f, 1) colour <br />
noiseMap[x][y] >= 0.62 --> //commented out so it does weird glitchy effect// colour <br />
noiseMap[x][y] >= 0.6 --> (0.5f, 0.5f, 0.3f, 1f) colour <br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />

<img src="https://user-images.githubusercontent.com/33546240/174501340-2efd9cd4-7937-4f8d-a4ab-f1f892786f26.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 1, 2f, 0.5f, 3, true <br /><br />
noiseMap[x][y] >= 0.96 --> (0.7f, 0.65f, 0.9f, 1) colour <br />
noiseMap[x][y] >= 0.62 --> //commented out so it does weird glitchy effect// colour <br />
noiseMap[x][y] >= 0.6 --> (0.5f, 0.5f, 0.3f, 1f) colour <br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />


<img src="https://user-images.githubusercontent.com/33546240/174501412-13c3bd7e-5bb7-4bc5-8223-a393a70a2c7e.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 1.7f, 0.3f, 2, true<br /><br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour <br />
background --> (0, 0.1f, 0.1f, 1) colour <br /><br /><br /><br />

<img src="https://user-images.githubusercontent.com/33546240/174501525-c29d988f-8077-4002-ad2e-a177d937bec1.png" width="662" height="391" />
seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 1.7f, 0.3f, -1, true<br /><br />
noiseMap[x][y] >= 0.5 --> (0.3f, 0.3f, 0.2f, 1) colour<br />
noiseMap[x][y] >= 0.16 && noiseMap[x][y] < 0.25 --> (0.5f, 0.5f, 0.3f, 1f) colour<br />
background --> 0, 0.1f, 0.1f, 1<br /><br /><br /><br />

