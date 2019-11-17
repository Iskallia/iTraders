package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;

public class TileEntityGeneratorBox extends TileInventoryBase {
	/*
	## "Generator Box"
	
	A block that generates RF / T, passively, outputs on all sides
	
	Right clicking brings up a UI that shows current RF Generation (Generating: X RF/T), 
	an input slot and list with 10 slots.
	
	Putting a Sub Egg in to the input slot consumes the subscriber and adds him to the 
	list like so: "<NAME> | $months RF / tick"
	
	The maximum amount of sub eggs per block is 10, this is so we dont need a scroll list. 
	
	The RF / T is the sum of $months inserted in the machine.
	*/
	
	public TileEntityGeneratorBox() {
		super(1);
	}
}
