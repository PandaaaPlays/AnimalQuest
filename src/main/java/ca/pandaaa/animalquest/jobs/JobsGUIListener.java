package ca.pandaaa.animalquest.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class JobsGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!JobsGUI.isJobsGUI(event.getView().getTitle())) {
            return;
        }
        event.setCancelled(true);
    }
}
