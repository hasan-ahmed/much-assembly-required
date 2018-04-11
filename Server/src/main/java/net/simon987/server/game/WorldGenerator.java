package net.simon987.server.game;

import net.simon987.server.assembly.exception.CancelledException;

public interface WorldGenerator {

    World generateWorld(int locX, int locY) throws CancelledException;

}
