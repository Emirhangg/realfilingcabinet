package com.bafomdad.realfilingcabinet.integration.ae2;

import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;

public interface IStorageBusMonitorWrapper {

	public IMEMonitor<IAEItemStack> createStorageBusMonitor(IMEInventory<IAEItemStack> inventory, BaseActionSource src);
}
