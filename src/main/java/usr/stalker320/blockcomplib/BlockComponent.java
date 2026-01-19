package usr.stalker320.blockcomplib;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public abstract class BlockComponent implements Component<ChunkStore> {
	@NullableDecl
	@Override
	public abstract BlockComponent clone();

	@NullableDecl
	@Override
	public Component<ChunkStore> cloneSerializable() {
		return this.clone();
	}
}
