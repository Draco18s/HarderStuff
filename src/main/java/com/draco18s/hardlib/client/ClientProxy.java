package com.draco18s.hardlib.client;

import com.draco18s.hardlib.CommonProxy;

public class ClientProxy extends CommonProxy {
	public void throwException() {
		throw new CogMissingException();
	}
}
