package usr.stalker320.blockcomplib;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.ISystem;
import com.hypixel.hytale.component.system.tick.ArchetypeTickingSystem;
import com.hypixel.hytale.protocol.Vector3i;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public abstract class BlockTickingSystem implements ISystem<ChunkStore> {
	public abstract void tick(
		int x, int y, int z,
		@Cancellable float dt,
		World world
	);

	public boolean test(Archetype<ChunkStore> archetype) {
		return getQuery() != null && getQuery().test(archetype);
	}


	@NullableDecl
	public abstract Query<ChunkStore> getQuery();
}
