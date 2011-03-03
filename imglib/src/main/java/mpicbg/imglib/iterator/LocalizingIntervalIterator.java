/**
 * Copyright (c) 2009--2011, Stephan Preibisch & Stephan Saalfeld
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the imglib project nor
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
 */
package mpicbg.imglib.iterator;

import mpicbg.imglib.Interval;
import mpicbg.imglib.Positionable;
import mpicbg.imglib.img.ImgRandomAccess;
import mpicbg.imglib.util.IntervalIndexer;

/**
 * Use this class to iterate a virtual {@link Interval} in flat order, that is:
 * row by row, plane by plane, cube by cube, ...  This is useful for iterating
 * an arbitrary interval in a defined order.  For that, connect a
 * {@link LocalizingIntervalIterator} to a {@link Positionable}.
 * 
 * <pre>
 * ...
 * LocalizingIntervalIterator i = new LocalizingIntervalIterator(image);
 * RandomAccess<T> s = image.randomAccess();
 * while (i.hasNext()) {
 *   i.fwd();
 *   s.setPosition(i);
 *   s.type().performOperation(...);
 *   ...
 * }
 * ...
 * </pre>
 * 
 * Note that {@link LocalizingIntervalIterator} is the right choice in
 * situations where, for <em>each</em> pixel, you want to localize and/or set
 * the {@link ImgRandomAccess}, that is in a dense sampling situation.  For
 * localizing sparsely (e.g. under an external condition), use
 * {@link IntervalIterator} instead.
 *  
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de>
 */
public class LocalizingIntervalIterator extends IntervalIterator
{
	final protected long[] position;
	
	public LocalizingIntervalIterator( final long[] dimensions )
	{
		super( dimensions );
		position = new long[ n ];
		reset();
	}
	
	public LocalizingIntervalIterator( final long[] min, final long[] max )
	{
		super( min, max );
		position = new long[ n ];
		reset();
	}

	public LocalizingIntervalIterator( final Interval interval )
	{
		super( interval );
		position = new long[ n ];
		reset();
	}
	

	/* Iterator */

	@Override
	public void fwd()
	{
		++index;

		for ( int d = 0; d < n; ++d )
		{
			if ( ++position[ d ] > max[ d ] )
				position[ d ] = min[ d ];
			else
				break;
		}
	}

	@Override
	public void jumpFwd( final long i )
	{
		index += i;
		IntervalIndexer.indexToPositionWithOffset( index, dimensions, min, position );
	}

	@Override
	public void reset()
	{
		index = -1;
		position[ 0 ] = min[ 0 ] - 1;

		for ( int d = 1; d < n; ++d )
			position[ d ] = min[ d ];
	}
	
	
	/* Localizable */
	
	@Override
	public void localize( final float[] pos )
	{
		for ( int d = 0; d < n; ++d )
			pos[ d ] = this.position[ d ];
	}

	@Override
	public void localize( final double[] pos )
	{
		for ( int d = 0; d < n; ++d )
			pos[ d ] = this.position[ d ];
	}

	@Override
	public void localize( final int[] pos )
	{
		for ( int d = 0; d < n; ++d )
			pos[ d ] = ( int )this.position[ d ];
	}
	
	@Override
	public void localize( final long[] pos )
	{
		for ( int d = 0; d < n; ++d )
			pos[ d ] = this.position[ d ];
	}
	
	@Override
	public float getFloatPosition( final int d )
	{
		return position[ d ];
	}
	
	@Override
	public double getDoublePosition( final int d )
	{
		return position[ d ];
	}
	
	@Override
	public int getIntPosition( final int d )
	{
		return ( int )position[ d ];
	}

	@Override
	public long getLongPosition( final int d )
	{
		return position[ d ];
	}
}
