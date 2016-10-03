package com.bafomdad.realfilingcabinet.api.common;

import java.util.UUID;

public interface ILockableCabinet {

	public UUID getOwner();
	
	public boolean setOwner(UUID owner);
	
	public boolean isCabinetLocked();
}
