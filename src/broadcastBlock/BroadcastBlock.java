package broadcastBlock;

import data.Block;

/**
 * Created by EnjoyD on 2017/4/20.
 */
public class BroadcastBlock {
    private Block block;
    public BroadcastBlock(Block block){
        this.block=block;
    }
    public void start(){
        broadcast();
    }

    private void broadcast() {
        System.out.println("broadcast over");
    }
}
