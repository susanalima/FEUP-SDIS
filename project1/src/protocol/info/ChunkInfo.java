package protocol.info;

import java.io.Serializable;

public class ChunkInfo implements Comparable<ChunkInfo>, Serializable {

    private String chunkKey;
    private Integer senderId;
    private Integer currRepDegree;
    private Integer desiredRepDegree;
    private boolean onPeer;
    private boolean handling;

    private String fileId;
    private int chunkNo;
    private int size;

    public ChunkInfo(String chunkKey, Integer senderId, Integer desiredRepDegree, int size, Integer currRepDegree, boolean onPeer) {
        this.chunkKey = chunkKey;
        this.senderId = senderId;
        this.currRepDegree = currRepDegree;
        this.desiredRepDegree = desiredRepDegree;
        this.size = size;
        this.onPeer = onPeer;
        this.handling = false;

        String[] info = chunkKey.split("_");
        fileId = info[0];
        chunkNo = Integer.parseInt(info[1]);
    }

    public String getChunkKey() {
        return chunkKey;
    }

    public void setChunkKey(String chunkKey) {
        this.chunkKey = chunkKey;
    }

    public Integer getCurrRepDegree() {
        return currRepDegree;
    }

    public void setCurrRepDegree(Integer currRepDegree) {
        this.currRepDegree = currRepDegree;
    }

    public Integer getDesiredRepDegree() {
        return desiredRepDegree;
    }

    public void setDesiredRepDegree(Integer desiredRepDegree) {
        this.desiredRepDegree = desiredRepDegree;
    }

    public String getFileId() {
        return fileId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public int getDifferenceBtCurrDesiredRepDegrees() {
        return currRepDegree - desiredRepDegree;
    }

    public boolean isOnPeer() {
        return onPeer;
    }

    public void setOnPeer(boolean onPeer) {
        this.onPeer = onPeer;
    }

    @Override
    public String toString() {
        return "ChunkInfo{" +
                "currRepDegree=" + currRepDegree +
                ", desiredRepDegree=" + desiredRepDegree +
                ", onPeer=" + onPeer +
                '}';
    }

    public boolean isHandling() {
        return handling;
    }

    public void setHandling(boolean handling) {
        this.handling = handling;
    }

    /*If the Integer is equal to the argument then 0 is returned.
    If the Integer is less than the argument then -1 is returned.
    If the Integer is greater than the argument then 1 is returned.

        1* apagar os em excesso de replicação mas com > excesso
        2* apagar os com replicação correta mas com > currRepDegree
        3* apagar os com defeito de replicação mas com > currRepDegree
    */
    @Override
    public int compareTo(ChunkInfo o) {
        if (this.currRepDegree.equals(o.getCurrRepDegree()) && this.desiredRepDegree.equals(o.getDesiredRepDegree()))
            return 0;
        Integer this_difference = this.getDifferenceBtCurrDesiredRepDegrees();
        Integer o_difference = o.getDifferenceBtCurrDesiredRepDegrees();

        if (this_difference > 0 && o_difference <= 0)
            return 1;

        if (this_difference > 0 && o_difference > 0)
            return this_difference.compareTo(o_difference);

        if (this_difference == 0 && o_difference < 0)
            return 1;

        if (this_difference == 0 && o_difference > 0)
            return -1;

        if (this_difference == 0 && o_difference == 0)
            return currRepDegree.compareTo(o.getCurrRepDegree());

        if (this_difference < 0 && o_difference < 0)
            return currRepDegree.compareTo(o.getCurrRepDegree());

        //if(this_difference < 0 && o_difference >= 0)
        return -1;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public int getSize() {
        return size;
    }
}

