package de.labathome;

import de.labathome.RemoteComputationKernelGrpc.RemoteComputationKernelBlockingStub;
import de.labathome.RemoteComputationKernelGrpc.RemoteComputationKernelStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RemoteComputationClient {


	private ManagedChannel channel;
	private RemoteComputationKernelBlockingStub blockingStub;
	private RemoteComputationKernelStub asyncStub;


	public RemoteComputationClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
	}

	/** Construct client for accessing RouteGuide server using the existing channel. */
	public RemoteComputationClient(ManagedChannelBuilder<?> channelBuilder) {
		channel = channelBuilder.build();
		blockingStub = RemoteComputationKernelGrpc.newBlockingStub(channel);
		asyncStub = RemoteComputationKernelGrpc.newStub(channel);
	}

}

