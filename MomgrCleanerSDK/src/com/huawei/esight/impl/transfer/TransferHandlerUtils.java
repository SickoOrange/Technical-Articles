package com.huawei.esight.impl.transfer;

import com.huawei.esight.constant.FileTransferPolicy;
import com.huawei.esight.service.IFileTransfer;

public class TransferHandlerUtils {


    public static IFileTransfer gethandler(FileTransferPolicy transferPolicy) {

        IFileTransfer transferHandler = null;
        switch (transferPolicy.getValue()) {
            case "byte":
                transferHandler = new ByteTransfer();
                break;
            case "buffer":
                transferHandler = new BufferTransfer();
                break;
            case "channel":
                transferHandler=new ChannelTransfer();
                break;
            case "nio":
                transferHandler=new NIOTransfer();
                break;
            default:
                transferHandler = new BufferTransfer();
        }

        return transferHandler;
    }

}
