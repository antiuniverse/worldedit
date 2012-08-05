// $Id$
/*
 * WorldEdit
 * Copyright (C) 2010 sk89q <http://www.sk89q.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.*;
import com.sk89q.worldedit.data.*;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Zach Brockway
 */
public class RedpowerMicroBlock extends BaseBlock implements TileEntityBlock {
    /**
     * Stores the micro block.
     */
	public final static int NUM_COVERS = 29;
    private int CoverSides;
    private short Covers[];

    /**
     * Construct the micro block.
     */
    public RedpowerMicroBlock() {
        super(BlockID.REDPOWER_MICRO);
        this.CoverSides = 0;
        this.Covers = new short[NUM_COVERS];
    }

    /**
     * Construct the micro block.
     *
     * @param data
     */
    public RedpowerMicroBlock(int data) {
        super(BlockID.REDPOWER_MICRO, data);
        this.CoverSides = 0;
        this.Covers = new short[NUM_COVERS];
    }

    public int GetCoverSides() { return CoverSides; }

    public void SetCoverSides(int newCoverSides) {
    	CoverSides = newCoverSides;
    }

    public short[] GetCovers() { return Covers; }

    public void SetCovers(short newCovers[]) {
    	Covers = newCovers;
    }

    /**
     * Return the name of the title entity ID.
     *
     * @return title entity ID
     */
    public String getTileEntityID() {
        return "RedpowerMicro";
    }

    /**
     * Store additional tile entity data.
     *
     * @return map of values
     * @throws DataException
     */
    public Map<String, Tag> toTileEntityNBT()
            throws DataException {
        Map<String, Tag> values = new HashMap<String, Tag>();

        values.put("cvm", new IntTag("cvm", CoverSides));

        byte coversArray[] = new byte[Integer.bitCount(CoverSides) * 2];
        int i = 0;
        for (int j = 0; j < NUM_COVERS; j++) {
        	if ( (CoverSides & 1 << j) != 0 ) {
        		coversArray[i]   = (byte)(Covers[j] & 0xFF);
        		coversArray[i+1] = (byte)(Covers[j] >> 8);
        		i += 2;
        	}
        }

        values.put("cvs", new ByteArrayTag("cvs", coversArray));

        return values;
    }

    /**
     * Get additional information from the title entity data.
     *
     * @param values
     * @throws DataException
     */
    public void fromTileEntityNBT(Map<String, Tag> values)
            throws DataException {
        if (values == null) {
            return;
        }

        Tag t;

        t = values.get("id");
        if (!(t instanceof StringTag) || !((StringTag) t).getValue().equals("RedpowerMicro")) {
            throw new DataException("'RedpowerMicro' tile entity expected");
        }

        t = values.get("cvm");
        if (t instanceof IntTag) {
            CoverSides = ((IntTag) t).getValue() & 0x1fffffff;
        }

        t = values.get("cvs");
        if (t instanceof ByteArrayTag) {
        	byte[] coversArray = ((ByteArrayTag) t).getValue();

        	if (coversArray != null && CoverSides > 0) {
        		int i = 0;
        		for (int j = 0; j < NUM_COVERS; j++) {
        			if ( (CoverSides & 1 << j) != 0 ) {
        				Covers[j] = (short)( (coversArray[i] & 0xFF) + ((coversArray[i+1] & 0xFF) << 8) );
        				i += 2;
        			}
        		}
        	}
        }
    }
}
