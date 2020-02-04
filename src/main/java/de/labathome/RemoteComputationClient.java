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
		asyncStub    = RemoteComputationKernelGrpc.newStub(channel);
	}

	/**
	 * reset the kernel to a known initial state
	 * - free memory of all stored variables
	 * - unload all shared libraries
	 * - ...
	 */
	public void reset() {
		blockingStub.reset(ResetRequest.newBuilder().build());
	}
//
//	public void sendScalarVariable(String name, double value) {
//		SendVariableRequest req = SendVariableRequest
//				.newBuilder()
//				.setVariableName(name)
//				.addDimensions(1)
//				.addData(value)
//				.build();
//		blockingStub.sendVariable(req);
//	}
//
//	public void sendVectorVariable(String name, double[] value) {
//		if (value != null && value.length>0) {
//			int len = value.length;
//
//			System.out.println("send 1d array of length "+len);
//
//			Builder req = SendVariableRequest.newBuilder();
//			req.setVariableName(name).addDimensions(len);
//			for (double d: value) {
//				req.addData(d);
//			}
//			blockingStub.sendVariable(req.build());
//		} else {
//			throw new RuntimeException("cannot send emtpy array!");
//		}
//	}
//
//	public void sendMatrixVariable(String name, double[][] value) {
//		if (value == null || value.length==0) {
//			throw new RuntimeException("cannot send emtpy array!");
//		} else {
//			int len0 = value.length;
//			int len1 = 0;
//			for (int i=0; i<len0; ++i) {
//				if (value[i] == null || value[i].length==0) {
//					throw new RuntimeException("cannot send emtpy array!");
//				} else {
//					if (i==0) len1 = value[0].length;
//					else if (len1 != value[i].length) {
//						throw new RuntimeException("cannot send jagged array!");
//					}
//				}
//			}
//
//			System.out.println("send 2d array "+len0+"x"+len1);
//
//			Builder req = SendVariableRequest.newBuilder();
//			req.setVariableName(name).addDimensions(len0).addDimensions(len1);
//			for (double[] d: value) {
//				for (double d1: d) {
//					req.addData(d1);
//				}
//			}
//			blockingStub.sendVariable(req.build());
//		}
//	}
//
//	public double getScalarVariable(String name) {
//		GetVariableRequest req = GetVariableRequest
//				.newBuilder()
//				.setVariableName(name)
//				.build();
//		GetVariableResult res = blockingStub.getVariable(req);
//		if (res.getStatus()==0) {
//			int numDims = res.getDimensionsCount();
//			if (numDims==1 && res.getDimensions(0)==1) {
//				return res.getData(0);
//			} else {
//				String err = "variable '"+name+"' has "+numDims+" wrong dimensions:";
//				for (int i=0; i<numDims; ++i) {
//					err += res.getDimensions(i)+" ";
//				}
//				throw new RuntimeException(err);
//			}
//		} else {
//			throw new RuntimeException("variable '"+name+"' not found");
//		}
//	}
//
//	public double[] getVectorVariable(String name) {
//		GetVariableRequest req = GetVariableRequest
//				.newBuilder()
//				.setVariableName(name)
//				.build();
//		GetVariableResult res = blockingStub.getVariable(req);
//		if (res.getStatus()==0) {
//			if (res.getDimensionsCount()==1) {
//				int n = res.getDimensions(0);
//				double[] ret = new double[n];
//				for (int i=0; i<n; ++i) {
//					ret[i] = res.getData(i);
//				}
//				return ret;
//			} else {
//				String err = "variable '"+name+"' has wrong dimensions:";
//				for (int i=0; i<res.getDimensionsCount(); ++i) {
//					err += res.getDimensions(i)+" ";
//				}
//				throw new RuntimeException(err);
//			}
//		} else {
//			throw new RuntimeException("variable '"+name+"' not found");
//		}
//	}
//	
//	public double[][] getMatrixVariable(String name) {
//		GetVariableRequest req = GetVariableRequest
//				.newBuilder()
//				.setVariableName(name)
//				.build();
//		GetVariableResult res = blockingStub.getVariable(req);
//		if (res.getStatus()==0) {
//			if (res.getDimensionsCount()==2) {
//				int n1 = res.getDimensions(0);
//				int n2 = res.getDimensions(1);
//				double[][] ret = new double[n1][n2];
//				for (int i=0; i<n1; ++i) {
//					for (int j=0; j<n2; ++j) {
//						ret[i][j] = res.getData(i*n2+j);
//					}
//				}
//				return ret;
//			} else {
//				String err = "variable '"+name+"' has wrong dimensions:";
//				for (int i=0; i<res.getDimensionsCount(); ++i) {
//					err += res.getDimensions(i)+" ";
//				}
//				throw new RuntimeException(err);
//			}
//		} else {
//			throw new RuntimeException("variable '"+name+"' not found");
//		}
//	}

	
	
	
	
	
	
	
	
	
	
	
	
	
//	
//
//
//	public void execute() {
//
//		ExecuteRequest req = ExecuteRequest.newBuilder().build();
//		ExecuteResult res;
//		try {
//			res = blockingStub.execute(req);
//		} catch (StatusRuntimeException e) {
//			System.out.println("RPC failed: "+e.getStatus());
//			e.printStackTrace();
//		}
//
//	}
//


	public static void main(String[] args) {

		// connect
		RemoteComputationClient client = new RemoteComputationClient("localhost", 50051);


		
		
		// clean environment
		client.reset();
//
//		client.sendScalarVariable("pi", Math.PI);
//		client.sendVectorVariable("twoTimesPi", new double[] {Math.PI, Math.PI});
//		client.sendMatrixVariable("matrix", new double[][] {{1,2},{3,4}});
//	
//		double pi = client.getScalarVariable("pi");
//		System.out.println("got pi from server: "+pi);
//		
//		double[] twoTimesPi = client.getVectorVariable("twoTimesPi");
//		for (double d: twoTimesPi) {
//			System.out.println("twoTimesPi has " + d);
//		}
//		
//		double[][] matrix_orig = client.getMatrixVariable("matrix");
//		System.out.println("matrix: ");
//		for (double[] v: matrix_orig) {
//			for (double d: v) {
//				System.out.print(d+" ");
//			}
//			System.out.println(" ");
//		}
//		
//		
//		
//		// execute some dynamically-loaded lapack method to compute a QR factorization of a 2x2 matrix called "matrix"
//		client.execute();

//
//		double[][] matrix = client.getMatrixVariable("matrix");
//		System.out.println("qr factorization of matrix: ");
//		for (double[] v: matrix) {
//			for (double d: v) {
//				System.out.print(d+" ");
//			}
//			System.out.println(" ");
//		}
//		
//		
//		
//		
//
//
//		client.reset();
	}

}

