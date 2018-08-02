package p455w0rd.biomestaff.init;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author p455w0rd
 *
 */
@EventBusSubscriber
public class ModEvents {

	@SubscribeEvent
	public static void onModelRegistryReady(ModelRegistryEvent event) {
		ModItems.BIOME_STAFF.initModel();
	}

	@SubscribeEvent
	public static void onItemRegistryReady(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(ModItems.BIOME_STAFF);
	}

}
