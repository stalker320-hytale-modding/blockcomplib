# BlockCompLib

Library for block components definitions.

# Build

## Gradlew

Just run

```sh
./gradlew build
```
At project directory and copy `build/libs/<pluginname>-<pluginversion>.jar` to your `Saves/<savename>/mods` or `Server/mods` folder.

## Prebuilt binaries

Download links:

Version | Link | Game version
------|-----|----
1.0.0 | https://github.com/stalker320-hytale-modding/blockcomplib/releases/download/v1.0.0/BlockCompLib-1.0.0.jar | 2026.01.17-4b0f30090

# Usage

Create new block component

```java
package com.example.components;

import usr.stalker320.blockcomplib.BlockComponent;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.codec.builder.BuilderCodec;

class MyBlockComponent extends BlockComponent {
	// Or specify fields using `.append(...).add()` before `.build()`
	public static final BuilderCodec<MyBlockComponent> CODEC
	= BuilderCodec.builder(MyBlockComponent.class, MyBlockComponent::new).build();

	public static HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
	private float time = 0.0;

	public MyBlockComponent(float t) {
		this.time = t;
	}
	public MyBlockComponent() {
		this(0.0f);
	}
	@Override
	public void run(int x, int y, int z, float delta, World world) {
		LOGGER.atInfo("MyBlockComponent at v3(x: " + x + ", y: " + y + ", z: " + z + ") time:" + this.time);
		// Just print position every tick.
		this.time += delta; // For block-changing use `world.execute(() -> {});`
	}
	@NullableDecl
	@Override
	public BlockComponent clone() {
		return new MyBlockComponent(this.time);
	}
}
```

Add it at main plugin file:

```java
package com.example;

// local dependency
import com.example.components.MyBlockComponent;

// usr.stalker320.blockcomplib dependency
import usr.stalker320.blockcomplib.BlockTickingSystem;

// Hytale dependency
import com.hypixel.hytale.component.ComponentType;

public class MyPlugin extends JavaPlugin {
	public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
	private ComponentType<ChunkStore, MyBlockComponent> myBlockComponentType;
	private BlockTickingSystem<MyBlockComponent> myBlockSystem;

	public MyPlugin(@Nonnull JavaPluginInit init) {
		super(init);
	}

	@Override
	protected void setup() {
		super.setup();
		LOGGER.at(Level.INFO).log("Plugin is setting up...");

    	this.spinComponentType = this.getChunkStoreRegistry()
				.registerComponent(MyBlockComponent.class, /*Name of field in your json file*/ "MyBlockComponent", MyBlockComponent.CODEC);
	    myBlockSystem = new BlockTickingSystem<MyBlockComponent>(this.myBlockComponentType);
	    this.getChunkStoreRegistry().registerSystem(myBlockSystem);
	    // Yeah, no need to inherit a BlockTickingSystem, logics moved to BlockComponent, but I recommend to save system to variable
		LOGGER.at(Level.INFO).log("Plugin setted up");
	}

	@Nonnull
	public ComponentType<ChunkStore, SpinComponent> getMyBlockComponentType() {
		return this.myBlockComponentType;
	}
}
```

Usage at json file (Used from my another project):

```json
{
  "TranslationProperties": {
    "Name": "items.stone_shaft.name",
    "Description": "items.stone_shaft.description"
  },
  "Id": "Stone_Shaft",
  "MaxStack": 400,

  "Icon": "Icons/ItemsGenerated/stone_shaft.png",

  "Categories": [
    "Blocks.Rocks"
  ],
  "PlayerAnimationsId": "Block",
  "Set": "Rock_Stone",
  "BlockType": {
    "Material": "Solid",
    "DrawType": "Model",
    "VariantRotation": "Pipe",
    "Group": "Stone",
    "Flags": {},
    "Gathering": {
      "Breaking": {
        "GatherType": "Rocks",
        "ItemId": "Rock_Stone_Cobble"
      }
    },
    "BlockParticleSetId": "Stone",
    "CustomModel": "Blocks/Machinery/stone_shaft.blockymodel",
    "CustomModelTexture": [
      {
        "All": "BlockTextures/stone_shaft.png"
      }
    ],
    "BlockEntity": {
      "Components": {
        "SpinComponent": { // Here it is!
          "velocity": 0.25
        }
      }
    },
    "ParticleColor": "#aeae8c",
    "BlockSoundSetId": "Stone",
    "BlockBreakingDecalId": "Breaking_Decals_Rock"
  }
}
```

# Credits

made by [stalker57241](https://github.com/stalker57241)
