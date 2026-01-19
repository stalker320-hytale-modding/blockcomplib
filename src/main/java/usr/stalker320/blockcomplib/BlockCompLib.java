package usr.stalker320.blockcomplib;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class BlockCompLib extends JavaPlugin {

	public BlockCompLib(@Nonnull JavaPluginInit init) {
		super(init);
	}
	@Override
	protected void setup() {
		super.setup();
		this.getChunkStoreRegistry()
			.registerSystem(new BlockSystem());
	}
}