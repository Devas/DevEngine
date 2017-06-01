# DevEngine
3D graphics engine built with Java and LWJGL 2.9.3

### Features:
* Loading .OBJ 3d models
* Batch rendering
* Terrain generated from heightmaps, terrain collision detection
* Textures, multitexturing, transparency, texture atlases, mipmapping
* Diffuse lighting, specular lighting, light presets
* Effects: fog
* Entities controlled by mouse or keyboard
* Different cameras
* GUI

### Build:
You have to use a VM option to specify a path to the natives:
* Windows: -Djava.library.path=native/windows/
* Linux: -Djava.library.path=native/linux/
* Mac OS X: -Djava.library.path=native/macosx/
* Solaris: -Djava.library.path=native/solaris/

### Screenshots:
<p align="center">
  <img src="https://cloud.githubusercontent.com/assets/9119159/26228091/ee1e01aa-3c36-11e7-8d0a-890e885fd7b8.png" width="900"/>
</p>
