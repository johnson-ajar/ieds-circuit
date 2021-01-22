package com.soa.circuit.common;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class CircuitMatrixUtil {
	private static DoubleFactory2D factory = DoubleFactory2D.sparse;
	
	public static DoubleMatrix2D appendRows(DoubleMatrix2D C, DoubleMatrix2D c){
			if(C == null){
				C = new SparseDoubleMatrix2D(c.toArray());
			}else{
				C = (SparseDoubleMatrix2D)factory.appendRows(C, c);
			}
		return C;
	}
	
	public static DoubleMatrix2D appendColumns(DoubleMatrix2D B, DoubleMatrix2D b){
		if(B == null){
			B = new SparseDoubleMatrix2D(b.toArray());
		}else{
			B = (SparseDoubleMatrix2D)factory.appendColumns(B,b);
		}
		return B;
	}
	
	public static DoubleMatrix2D zeroes(int rows, int columns){
		return factory.make(rows, columns, 0.0);
	}
}
