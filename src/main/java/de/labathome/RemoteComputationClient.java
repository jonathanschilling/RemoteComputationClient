package de.labathome;

import de.labathome.RemoteComputationKernelGrpc.RemoteComputationKernelBlockingStub;
import de.labathome.RemoteComputationKernelGrpc.RemoteComputationKernelStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

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

	public void reset() {

		ResetRequest req = ResetRequest.newBuilder().build();
		ResetResult result;
		try {
			result = blockingStub.reset(req);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC failed: "+e.getStatus());
			e.printStackTrace();
		}

	}

	public void sendSomeData() {

		SendVariableRequest req = SendVariableRequest
				.newBuilder()
				.setVariableName("testVariable")
				.addData(42.0)
				.addData(41.0)
				.build();
		SendVariableResult result;
		try {
			result = blockingStub.sendVariable(req);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC failed: "+e.getStatus());
			e.printStackTrace();
		}
	}

	public void execute() {
		
		ExecuteRequest req = ExecuteRequest.newBuilder().build();
		ExecuteResult res;
		try {
			res = blockingStub.execute(req);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC failed: "+e.getStatus());
			e.printStackTrace();
		}
		
	}
	
	public void queryData() {

		String varName = "testVariable";

		GetVariableRequest req = GetVariableRequest
				.newBuilder()
				.setVariableName(varName)
				.build();
		GetVariableResult res;
		try {
			res = blockingStub.getVariable(req);

			System.out.println("got data for variable "+varName);
			if (res.getStatus()==0) {
				for (int i=0; i<res.getDataCount(); ++i) {
					System.out.println(varName+"["+i+"]="+res.getData(i));
				}
			} else {
				System.out.println(varName+" not found");
			}
		} catch (StatusRuntimeException e) {
			System.out.println("RPC failed: "+e.getStatus());
			e.printStackTrace();
		}

	}

	
	

	public static void main(String[] args) {

		// connect
		RemoteComputationClient client = new RemoteComputationClient("localhost", 50051);


		// clean environment
		client.reset();

		client.sendSomeData();

		client.execute();
		
		client.queryData();

	}

}

