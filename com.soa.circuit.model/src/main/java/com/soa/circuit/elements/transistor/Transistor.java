package com.soa.circuit.elements.transistor;

import com.soa.circuit.common.ElementType;
import com.soa.circuit.common.NonLinearCircuitElement;
import com.soa.circuit.exception.ModelReaderException;
import com.soa.circuit.model.AnalysisType;
import com.soa.circuit.model.CircuitModel;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.persist.CircuitPersistanceGremlinFactory;

/*There are two model used 1. Ebbers Molls model.
 * Gummel Poon model.
 *
 *http://www.youspice.com/spice-modeling-of-a-bjt-from-datasheet/
IS	Transport saturation current	A	1e-16
XTI	temperature effect exponent	no unit dimension	3.0
EG	Bandgap voltage (barrier height)	eV	1.11
VAF	Forward Early voltage	V	Infinite
BF	Ideal maximum forward beta	no unit dimension	100
ISE	Base-emitter leakage saturation current	A	0
NE	Base-emitter leakage emission coefficient	no unit dimension	1.5
IKF	Corner for forward-beta high-current roll-off	A	Infinite
NK	High-current roll-off coefficient	no unit dimension	0.5
XTB	Forward and reverse beta temperature coefficient	no unit dimension	0
BR	Ideal maximum reverse beta	no unit dimension	1.0
ISC	Base-collector leakage saturation current	A	0
NC	Base-collector leakage emission coefficient	no unit dimension	2.0
IKR	Corner for reverse-beta high-current roll-off	A	Infinite
RC	Collector ohmic resistance	Ohm	0
CJC	Base-collector zero-bias p-n capacitance	F	0
MJC	Base-collector p-n grading factor	no unit dimension	0.33
VJC		V	0.75
FC	Forward-bias depletion capacitor coefficient	no unit dimension	0.5
CJE	Base-emitter zero-bias p-n capacitance	F	0
MJE	Base-emitter p-n grading factor	no unit dimension	0.33
VJE	Base-emitter built-in potential	V	0.75
TR	Ideal reverse transit time	sec	1e-8
TF	Ideal forward transit time	sec	0
ITF	Transit time dependency on Ic	A	0
XTF	Transit time bias dependence coefficient	no unit dimension	0
VTF	Transit time dependency on Vbc	V	Infinite
RB	Zero-bias (maximum) base resistance	Ohm	0

 
 
 * */
public class Transistor extends NonLinearCircuitElement{

	public Transistor(CircuitModel inModel) {
		super(inModel);
	}

	@Override
	public ElementType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNoNodes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected CircuitElementVertex save(CircuitPersistanceGremlinFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(CircuitElementVertex vertex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(AnalysisType inType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculateCurrent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrent(double inCurrent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void parseElement(String[] values) throws ModelReaderException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(double inValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getConductance() {
		// TODO Auto-generated method stub
		return 0;
	}

}
