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

import com.example.MyPlugin;

import usr.stalker320.blockcomplib.BlockComponent;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.codec.builder.BuilderCodec;

class MyBlockComponent extends BlockComponent {
	// Or specify fields using `.append(...).add()` before `.build()`
	public static final BuilderCodec<MyBlockComponent> CODEC
		= BuilderCodec.builder(MyBlockComponent.class, MyBlockComponent::new).build();

	public static HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
	private float time = 0.0f;

	public static ComponentType<ChunkType, MyBlockComponent> getComponentType() {
		return MyPlugin.getInstance().getMyBlockComponentType();
	}

	public float getTime() {
		return this.time;
	}
	public void setTime(float value) {
		this.time = value;
	}

	public MyBlockComponent(float t) {
		this.time = t;
	}
	public MyBlockComponent() {
		this(0.0f);
	}
	@NullableDecl
	@Override
	public BlockComponent clone() {
		return new MyBlockComponent(this.time);
	}
}
```

Add system:
```java
package com.example.systems;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import usr.stalker320.blockcomplib.BlockTickingSystem;


public class MyBlockSystem extends BlockTickingSystem {
	public static HytaleLogger LOGGER = Machinery.LOGGER.getSubLogger("MyBlockSystem");
	private static MyBlockSystem INSTANCE;

	public MyBlockSystem() {
		super();
		INSTANCE = this;
	}

	@Override
	public void tick(int x, int y, int z, float delta, World world) {
		world.execute(() -> {
			Holder<ChunkStore> blockData = world.getBlockComponentHolder(x, y, z);
			assert blockData != null;
			MyBlockComponent component = blockData.getComponent(MyBlockComponent.getComponentType());
			component.setTime(component.getTime() + delta);
			LOGGER.atInfo().log("MyBlockSystem at: (x: " + x + ", y: " + y + ", z: " + z + ") time: " + component.getTime());
		});
	}

	@NullableDecl
	@Override
	public Query<ChunkStore> getQuery() {
		return Query.and(MyBlockComponent.getComponentType());
	}

}
```
Add it at main plugin file:

```java
package com.example;

// local dependency
import com.example.components.MyBlockComponent;
import com.example.systems.MyBlockSystem;

// usr.stalker320.blockcomplib dependency
import usr.stalker320.blockcomplib.BlockSystem;

// Hytale dependency
import com.hypixel.hytale.component.ComponentType;

public class MyPlugin extends JavaPlugin {
	public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
	private ComponentType<ChunkStore, MyBlockComponent> myBlockComponentType;

	private static MyPlugin instance;

	public static MyPlugin getInstance() {
		return instance;
	}
	public MyPlugin(@Nonnull JavaPluginInit init) {
		super(init);
		instance = this;
	}

	@Override
	protected void setup() {
		super.setup();

    	this.spinComponentType = this.getChunkStoreRegistry()
				.registerComponent(
					MyBlockComponent.class,
					/*Name of field in your json file*/ "MyBlockComponent",
					MyBlockComponent.CODEC
				);

		BlockSystem.registerSystem(new MyBlockSystem());
	
		LOGGER.at(Level.INFO).log("Plugin is setting up...");
	}

	@Nonnull
	public ComponentType<ChunkStore, SpinComponent> getMyBlockComponentType() {
		return this.myBlockComponentType;
	}
}
```

Usage at json file:

```json
{
  "...": "...",
  "BlockType": {
  	"...": "...",
    "BlockEntity": {
      "Components": {
		"...": {},
        "MyBlockComponent": { "__comment": "Here!!!" },
		"...": {}
      }
    },
	"...": "..."
  }
}
```

# Credits

made by [stalker57241](https://github.com/stalker57241)
