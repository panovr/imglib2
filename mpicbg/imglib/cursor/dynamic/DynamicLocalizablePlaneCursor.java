/**
 * Copyright (c) 2009--2010, Stephan Preibisch
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the Fiji project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Stephan Preibisch
 */
package mpicbg.imglib.cursor.dynamic;

import mpicbg.imglib.container.dynamic.DynamicContainer;
import mpicbg.imglib.cursor.LocalizablePlaneCursor;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.type.Type;

public class DynamicLocalizablePlaneCursor<T extends Type<T>> extends DynamicLocalizableCursor<T> implements LocalizablePlaneCursor<T>
{
	protected int planeDimA, planeDimB, planeSizeA, planeSizeB, incPlaneA, incPlaneB, maxI;
	
	public DynamicLocalizablePlaneCursor( final DynamicContainer<T> container, final Image<T> image, final T type ) 
	{
		super( container, image, type );
	}	
	
	@Override 
	public boolean hasNext() { return internalIndex < maxI; }
	
	@Override
	public void fwd()
	{ 
		if ( position[ planeDimA ] < dimensions[ planeDimA ] - 1)
		{
			position[ planeDimA ]++;
			internalIndex += incPlaneA;
		}
		else if ( position[ planeDimB ] < dimensions[ planeDimB ] - 1)
		{
			position[ planeDimA ] = 0;
			position[ planeDimB ]++;
			
			internalIndex += incPlaneB;
			internalIndex -= (planeSizeA - 1) * incPlaneA;
		}
		type.updateDataArray( this );				
	}
	
	@Override
	public void reset( final int planeDimA, final int planeDimB, final int[] dimensionPositions )
	{
		this.planeDimA = planeDimA;
		this.planeDimB = planeDimB;
		
		this.planeSizeA = container.getDimension( planeDimA );
		this.planeSizeB = container.getDimension( planeDimB );
		
		final int[] steps = container.getSteps();

		// store the current position
    	final int[] dimPos = dimensionPositions.clone();
		
		incPlaneA = steps[ planeDimA ];
		dimPos[ planeDimA ] = 0;
		
		if ( planeDimB > -1 && planeDimB < steps.length )
		{
			incPlaneB = steps[ planeDimB ];
			dimPos[ planeDimB ] = 0;
		}
		else
		{
			incPlaneB = 0;
		}

		setPosition( dimPos );		
		isClosed = false;
		
		type.updateDataArray( this );				
		internalIndex -= incPlaneA;					
		position[ planeDimA ] = -1;
		
		dimPos[ planeDimA ] = dimensions[ planeDimA ] - 1;		
		if ( planeDimB > -1 && planeDimB < steps.length )
			dimPos[ planeDimB ] = dimensions[ planeDimB ] - 1;
		
		maxI = container.getPos( dimPos );		
	}

	@Override
	public void reset( final int planeDimA, final int planeDimB )
	{
		if ( dimensions == null )
			return;

		reset( planeDimA, planeDimB, new int[ numDimensions ] );
	}
	
	@Override
	public void reset()
	{
		if ( dimensions == null )
			return;
		
		reset( 0, 1, image.createPositionArray() );		
	}

	@Override
	public void getPosition( int[] position )
	{
		for ( int d = 0; d < numDimensions; d++ )
			position[ d ] = this.position[ d ];
	}
	
	@Override
	public int[] getPosition(){ return position.clone(); }
	
	@Override
	public int getPosition( final int dim ){ return position[ dim ]; }
	
	protected void setPosition( final int[] position )
	{
		internalIndex = container.getPos( position );
		type.updateDataArray( this );				
		
		for ( int d = 0; d < numDimensions; d++ )
			this.position[ d ] = position[ d ];
	}
	
}
