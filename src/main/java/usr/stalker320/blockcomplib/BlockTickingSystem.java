package usr.stalker320.blockcomplib;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.asset.type.blocktick.BlockTickStrategy;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.chunk.section.ChunkSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class BlockTickingSystem<T extends BlockComponent> extends EntityTickingSystem<ChunkStore> {
	private static final Query<ChunkStore> QUERY
		= Query.and(
			BlockSection.getComponentType(),
			ChunkSection.getComponentType()
		);
	private final ComponentType<ChunkStore, T> blockComponentType;

	public BlockTickingSystem(ComponentType<ChunkStore, T> blockComponentType) {
		this.blockComponentType = blockComponentType;
	}

	@Override
	public void tick(float dt, int idx, @NonNullDecl ArchetypeChunk<ChunkStore> archetypeChunk, @NonNullDecl Store<ChunkStore> store, @NonNullDecl CommandBuffer<ChunkStore> commandBuffer) {
		BlockSection blocks = archetypeChunk.getComponent(idx, BlockSection.getComponentType());

		assert blocks != null;

		if (blocks.getTickingBlocksCountCopy() != 0) {
			ChunkSection section = archetypeChunk.getComponent(idx, ChunkSection.getComponentType());

			assert section != null;

			BlockComponentChunk blockComponentChunk
				= commandBuffer.getComponent(
				section.getChunkColumnReference(),
				BlockComponentChunk.getComponentType()
			);
			assert blockComponentChunk != null;

			blocks.forEachTicking(
				blockComponentChunk,
				commandBuffer,
				section.getY(),
				(
					blockComponentChunk1,
					commandBuffer1,
					localX, localY, localZ,
					blockId
				) -> {
					Ref<ChunkStore> blockRef
						= blockComponentChunk1.getEntityReference(ChunkUtil.indexBlockInColumn(localX, localY, localZ));
					if (blockRef == null) return BlockTickStrategy.IGNORED;
					T component
						= commandBuffer.getComponent(blockRef, this.blockComponentType);
					if (component == null) return BlockTickStrategy.IGNORED;
					WorldChunk worldChunk = commandBuffer.getComponent(section.getChunkColumnReference(), WorldChunk.getComponentType());

					assert worldChunk != null;

					int globalX = localX + (worldChunk.getX() * 32);
					int globalZ = localZ + (worldChunk.getZ() * 32);

					component.run(globalX, localY, globalZ, dt, worldChunk.getWorld());

					return BlockTickStrategy.CONTINUE;
				}
			);
		}
	}

	@NullableDecl
	@Override
	public Query<ChunkStore> getQuery() {
		return QUERY;
	}
}
