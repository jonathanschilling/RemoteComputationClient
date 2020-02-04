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

	/**
	 * allocate a scalar integer on the server
	 * @return id of the variable on the server
	 */
	public int allocate_int() {
		return allocate_int(null);
	}

	/**
	 * allocate a (multi-dimensional) integer variable on the server
	 * @param dimensions (list of) array size(s)
	 * @return id of the variable on the server
	 */
	public int allocate_int(final int... dimensions) {

		AlocVarRequest.Builder req_builder = AlocVarRequest
				.newBuilder()
				.setDtype(3);

		if (dimensions != null) {
			for (int dim: dimensions) {
				req_builder.addDimensions(dim);
			}
		}

		AlocVarResult res = blockingStub.alocVar(req_builder.build());

		if (res.getError() == 0) {
			return res.getId();
		} else {
			throw new RuntimeException("alocVar(int) failed with the following error code: "+res.getError());
		}
	}

	/**
	 * allocate a scalar float on the server
	 * @return id of the variable on the server
	 */
	public int allocate_float() {
		return allocate_float(null);
	}

	/**
	 * allocate a (multi-dimensional) float variable on the server
	 * @param dimensions (list of) array size(s)
	 * @return id of the variable on the server
	 */
	public int allocate_float(final int... dimensions) {

		AlocVarRequest.Builder req_builder = AlocVarRequest
				.newBuilder()
				.setDtype(5);

		if (dimensions != null) {
			for (int dim: dimensions) {
				req_builder.addDimensions(dim);
			}
		}

		AlocVarResult res = blockingStub.alocVar(req_builder.build());

		if (res.getError() == 0) {
			return res.getId();
		} else {
			throw new RuntimeException("alocVar(float) failed with the following error code: "+res.getError());
		}
	}

	/**
	 * allocate a scalar double on the server
	 * @return id of the variable on the server
	 */
	public int allocate_double() {
		return allocate_double(null);
	}

	/**
	 * allocate a (multi-dimensional) double variable on the server
	 * @param dimensions (list of) array size(s)
	 * @return id of the variable on the server
	 */
	public int allocate_double(final int... dimensions) {

		AlocVarRequest.Builder req_builder = AlocVarRequest
				.newBuilder()
				.setDtype(6);

		if (dimensions != null) {
			for (int dim: dimensions) {
				req_builder.addDimensions(dim);
			}
		}

		AlocVarResult res = blockingStub.alocVar(req_builder.build());

		if (res.getError() == 0) {
			return res.getId();
		} else {
			throw new RuntimeException("alocVar(double) failed with the following error code: "+res.getError());
		}
	}

	/**
	 * Release the memory associated with a given id on the server.
	 * @param id of the variable to free
	 */
	public void free_variable(final int id) {

		FreeVarRequest req = FreeVarRequest.newBuilder()
				.setId(id)
				.build();

		FreeVarResult res = blockingStub.freeVar(req);

		if (res.getError() != 0) {
			throw new RuntimeException("freeVar("+id+") failed with the following error code: "+res.getError());
		}
	}





	public void send_int(final int id, final int value) {

		SendIntRequest req = SendIntRequest
				.newBuilder()
				.setId(id)
				.addData(value)
				.build();

		SendIntResult res = blockingStub.sendInt(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendInt("+id+", int) failed with the following error code: "+res.getError());
		}
	}

	public void send_int(final int id, final int[] value) {

		SendIntRequest.Builder req_builder = SendIntRequest
				.newBuilder()
				.setId(id);
		for (int i=0; i<value.length; ++i) {
			req_builder.addData(value[i]);
		}
		SendIntRequest req = req_builder.build();

		SendIntResult res = blockingStub.sendInt(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendInt("+id+", int[]) failed with the following error code: "+res.getError());
		}
	}

	public void send_int(final int id, final int[][] value) {

		SendIntRequest.Builder req_builder = SendIntRequest
				.newBuilder()
				.setId(id);

		// COLUMN_MAJOR for Fortran routines
		for (int i=0; i<value.length; ++i) {
			for (int j=0; j<value[0].length; ++j) {
				req_builder.addData(value[j][i]);
			}
		}

		SendIntRequest req = req_builder.build();

		SendIntResult res = blockingStub.sendInt(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendInt("+id+", int[][]) failed with the following error code: "+res.getError());
		}
	}



	public void send_float(final int id, final float value) {

		SendFltRequest req = SendFltRequest
				.newBuilder()
				.setId(id)
				.addData(value)
				.build();

		SendFltResult res = blockingStub.sendFlt(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendFlt("+id+", float) failed with the following error code: "+res.getError());
		}
	}

	public void send_float(final int id, final float[] value) {

		SendFltRequest.Builder req_builder = SendFltRequest
				.newBuilder()
				.setId(id);
		for (int i=0; i<value.length; ++i) {
			req_builder.addData(value[i]);
		}
		SendFltRequest req = req_builder.build();

		SendFltResult res = blockingStub.sendFlt(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendFlt("+id+", float[]) failed with the following error code: "+res.getError());
		}
	}

	public void send_float(final int id, final float[][] value) {

		SendFltRequest.Builder req_builder = SendFltRequest
				.newBuilder()
				.setId(id);

		// COLUMN_MAJOR for Fortran routines
		for (int i=0; i<value.length; ++i) {
			for (int j=0; j<value[0].length; ++j) {
				req_builder.addData(value[j][i]);
			}
		}

		SendFltRequest req = req_builder.build();

		SendFltResult res = blockingStub.sendFlt(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendFlt("+id+", float[][]) failed with the following error code: "+res.getError());
		}
	}

	public void send_double(final int id, final double value) {

		SendDblRequest req = SendDblRequest
				.newBuilder()
				.setId(id)
				.addData(value)
				.build();

		SendDblResult res = blockingStub.sendDbl(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendDbl("+id+", double) failed with the following error code: "+res.getError());
		}
	}

	public void send_double(final int id, final double[] value) {

		SendDblRequest.Builder req_builder = SendDblRequest
				.newBuilder()
				.setId(id);
		for (int i=0; i<value.length; ++i) {
			req_builder.addData(value[i]);
		}
		SendDblRequest req = req_builder.build();

		SendDblResult res = blockingStub.sendDbl(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendDbl("+id+", double[]) failed with the following error code: "+res.getError());
		}
	}

	public void send_double(final int id, final double[][] value) {

		SendDblRequest.Builder req_builder = SendDblRequest
				.newBuilder()
				.setId(id);

		// COLUMN_MAJOR for Fortran routines on the server side
		for (int i=0; i<value.length; ++i) {
			for (int j=0; j<value[0].length; ++j) {
				req_builder.addData(value[j][i]);
			}
		}

		SendDblRequest req = req_builder.build();

		SendDblResult res = blockingStub.sendDbl(req);

		if (res.getError() != 0) {
			throw new RuntimeException("sendDbl("+id+", double[][]) failed with the following error code: "+res.getError());
		}
	}




	public final int recv_int_0d(final int id) {

		RecvIntRequest req = RecvIntRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvIntResult res = blockingStub.recvInt(req);
		
		if (res.getError() == 0) {
			int num_data = res.getDataCount();
			
			if (num_data == 1) {
				return res.getData(0);
			} else {
				throw new RuntimeException("recvInt("+id+") delivered "+num_data+" values instead of 1");
			}
		} else {
			throw new RuntimeException("recvInt("+id+") failed with the following error code: "+res.getError());
		}
	}

	public final int[] recv_int_1d(final int id) {
		RecvIntRequest req = RecvIntRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvIntResult res = blockingStub.recvInt(req);
		if (res.getError() == 0) {
			
			int num_dims = res.getDimensionsCount();
			if (num_dims == 1) {
				
				int num_data = res.getDataCount();
				if (num_data > 0 && num_data == res.getDimensions(0)) {
					
					final int[] result = new int[num_data];
					for (int i=0; i<num_data; ++i) {
						result[i] = res.getData(i);
					}
					
					return result;
					
				} else {
					throw new RuntimeException("recvInt("+id+") delivered "+num_data+" values; should be "+res.getDimensions(0));
				}
			} else {
				throw new RuntimeException("recvInt("+id+") call gave too many dimensions: "+num_dims);
			}
		} else {
			throw new RuntimeException("recvInt("+id+") failed with the following error code: "+res.getError());
		}
	}

	public final int[][] recv_int_2d(final int id) {
		RecvIntRequest req = RecvIntRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvIntResult res = blockingStub.recvInt(req);
		if (res.getError() == 0) {
			
			int num_dims = res.getDimensionsCount();
			if (num_dims == 2) {
				
				int dim0 = res.getDimensions(0);
				int dim1 = res.getDimensions(1);
				int num_total = dim0*dim1;
				
				int num_data = res.getDataCount();
				if (num_data > 0 && num_data == num_total) {

					// COLUMN_MAJOR for Fortran routines on the server side
					final int[][] result = new int[dim0][dim1];
					int idx=0;
					for (int j=0; j<dim1; ++j) {
						for (int i=0; i<dim0; ++i) {
							result[i][j] = res.getData(idx);
							idx++;
						}
					}
					
					return result;
					
				} else {
					throw new RuntimeException("recvInt("+id+") delivered "+num_data+" values; should be "+num_total);
				}
			} else {
				throw new RuntimeException("recvInt("+id+") call gave too many dimensions: "+num_dims);
			}
		} else {
			throw new RuntimeException("recvInt("+id+") failed with the following error code: "+res.getError());
		}
	}



	public final float recv_float_0d(final int id) {

		RecvFltRequest req = RecvFltRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvFltResult res = blockingStub.recvFlt(req);
		
		if (res.getError() == 0) {
			int num_data = res.getDataCount();
			
			if (num_data == 1) {
				return res.getData(0);
			} else {
				throw new RuntimeException("recvFlt("+id+") delivered "+num_data+" values instead of 1");
			}
		} else {
			throw new RuntimeException("recvFlt("+id+") failed with the following error code: "+res.getError());
		}
	}

	public final float[] recv_float_1d(final int id) {
		RecvFltRequest req = RecvFltRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvFltResult res = blockingStub.recvFlt(req);
		if (res.getError() == 0) {
			
			int num_dims = res.getDimensionsCount();
			if (num_dims == 1) {
				
				int num_data = res.getDataCount();
				if (num_data > 0 && num_data == res.getDimensions(0)) {
					
					final float[] result = new float[num_data];
					for (int i=0; i<num_data; ++i) {
						result[i] = res.getData(i);
					}
					
					return result;
					
				} else {
					throw new RuntimeException("recvFlt("+id+") delivered "+num_data+" values; should be "+res.getDimensions(0));
				}
			} else {
				throw new RuntimeException("recvFlt("+id+") call gave too many dimensions: "+num_dims);
			}
		} else {
			throw new RuntimeException("recvFlt("+id+") failed with the following error code: "+res.getError());
		}
	}

	public final float[][] recv_float_2d(final int id) {
		RecvFltRequest req = RecvFltRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvFltResult res = blockingStub.recvFlt(req);
		if (res.getError() == 0) {
			
			int num_dims = res.getDimensionsCount();
			if (num_dims == 2) {
				
				int dim0 = res.getDimensions(0);
				int dim1 = res.getDimensions(1);
				int num_total = dim0*dim1;
				
				int num_data = res.getDataCount();
				if (num_data > 0 && num_data == num_total) {

					// COLUMN_MAJOR for Fortran routines on the server side
					final float[][] result = new float[dim0][dim1];
					int idx=0;
					for (int j=0; j<dim1; ++j) {
						for (int i=0; i<dim0; ++i) {
							result[i][j] = res.getData(idx);
							idx++;
						}
					}
					
					return result;
					
				} else {
					throw new RuntimeException("recvFlt("+id+") delivered "+num_data+" values; should be "+num_total);
				}
			} else {
				throw new RuntimeException("recvFlt("+id+") call gave too many dimensions: "+num_dims);
			}
		} else {
			throw new RuntimeException("recvFlt("+id+") failed with the following error code: "+res.getError());
		}
	}




	public final double recv_double_0d(final int id) {

		RecvDblRequest req = RecvDblRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvDblResult res = blockingStub.recvDbl(req);
		
		if (res.getError() == 0) {
			int num_data = res.getDataCount();
			
			if (num_data == 1) {
				return res.getData(0);
			} else {
				throw new RuntimeException("recvDbl("+id+") delivered "+num_data+" values instead of 1");
			}
		} else {
			throw new RuntimeException("recvDbl("+id+") failed with the following error code: "+res.getError());
		}
	}

	public final double[] recv_double_1d(final int id) {
		RecvDblRequest req = RecvDblRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvDblResult res = blockingStub.recvDbl(req);
		if (res.getError() == 0) {
			
			int num_dims = res.getDimensionsCount();
			if (num_dims == 1) {
				
				int num_data = res.getDataCount();
				if (num_data > 0 && num_data == res.getDimensions(0)) {
					
					final double[] result = new double[num_data];
					for (int i=0; i<num_data; ++i) {
						result[i] = res.getData(i);
					}
					
					return result;
					
				} else {
					throw new RuntimeException("recvDbl("+id+") delivered "+num_data+" values; should be "+res.getDimensions(0));
				}
			} else {
				throw new RuntimeException("recvDbl("+id+") call gave too many dimensions: "+num_dims);
			}
		} else {
			throw new RuntimeException("recvDbl("+id+") failed with the following error code: "+res.getError());
		}
	}

	public final double[][] recv_double_2d(final int id) {
		RecvDblRequest req = RecvDblRequest
				.newBuilder()
				.setId(id)
				.build();
			
		RecvDblResult res = blockingStub.recvDbl(req);
		if (res.getError() == 0) {
			
			int num_dims = res.getDimensionsCount();
			if (num_dims == 2) {
				
				int dim0 = res.getDimensions(0);
				int dim1 = res.getDimensions(1);
				int num_total = dim0*dim1;
				
				int num_data = res.getDataCount();
				if (num_data > 0 && num_data == num_total) {

					// COLUMN_MAJOR for Fortran routines on the server side
					final double[][] result = new double[dim0][dim1];
					int idx=0;
					for (int j=0; j<dim1; ++j) {
						for (int i=0; i<dim0; ++i) {
							result[i][j] = res.getData(idx);
							idx++;
						}
					}
					
					return result;
					
				} else {
					throw new RuntimeException("recvDbl("+id+") delivered "+num_data+" values; should be "+num_total);
				}
			} else {
				throw new RuntimeException("recvDbl("+id+") call gave too many dimensions: "+num_dims);
			}
		} else {
			throw new RuntimeException("recvDbl("+id+") failed with the following error code: "+res.getError());
		}
	}






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

		double a_send = 42.0;
		
		int id_a = client.allocate_double();
		client.send_double(id_a, a_send);
		
		double a_readback = client.recv_double_0d(id_a);
		
		System.out.println("a send="+a_send+" readback="+a_readback);


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

