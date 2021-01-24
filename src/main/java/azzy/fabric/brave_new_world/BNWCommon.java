package azzy.fabric.brave_new_world;

import azzy.fabric.brave_new_world.api.RotAPI;
import azzy.fabric.brave_new_world.components.TemperatureComponent;
import azzy.fabric.brave_new_world.item.BNWItems;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class BNWCommon implements ModInitializer {

	public static final String MOD_ID = "brave_new_world";

	public static final Logger BNW_LOG = LogManager.getLogger("Brave New World");

	public static final ItemGroup BNW_ITEMS = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "items"), () -> new ItemStack(Items.HONEY_BOTTLE));

	@Override
	public void onInitialize() {
		BNWItems.init();
		BNWWorldEvents.init();

		ResourceManagerHelperImpl.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(MOD_ID, "bnw_resources");
			}

			@Override
			public void apply(ResourceManager manager) {
				RotAPI.load(manager);
			}
		});
	}
}
