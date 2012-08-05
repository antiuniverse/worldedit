// $Id$

package com.sk89q.worldedit.redpower;

import org.bukkit.World;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;

import eloraam.core.TileCovered;
import org.bukkit.craftbukkit.CraftWorld;
import net.minecraft.server.TileEntity;

/**
 * Various hook methods for supporting RedPower-specific behaviors.
 *
 * @author Zach Brockway
 */
public class RedPowerHooks {
    public static BaseBlock rawGetBlock(int type, int data, Vector pt, LocalWorld world) {
        switch (type) {
        case RedPowerBlockID.MICRO: {
            RedPowerMicroBlock block = new RedPowerMicroBlock(data);
            world.copyFromWorld(pt, block);
            return block;
        }
        }

        return null;
    }

    public static boolean copyFromWorld(World world, Vector pt, BaseBlock block) {
        TileEntity te = ((CraftWorld) world).getTileEntityAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
        if (te instanceof TileCovered) {
            TileCovered tc = (TileCovered)te;
            RedPowerMicroBlock we = (RedPowerMicroBlock) block;
            we.SetCoverSides(tc.CoverSides);
            we.SetCovers(tc.Covers.clone());
            return true;
        }

        return false;
    }

    public static boolean copyToWorld(World world, Vector pt, BaseBlock block) {
        TileEntity te = ((CraftWorld) world).getTileEntityAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
        if (te instanceof TileCovered) {
            TileCovered tc = (TileCovered)te;
            RedPowerMicroBlock we = (RedPowerMicroBlock) block;
            tc.CoverSides = we.GetCoverSides();
            tc.Covers = we.GetCovers().clone();
            tc.updateBlockChange();
            return true;
        }

        return false;
    }
}
