/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package net.imglib2.ops.operation.iterableinterval.unary;

import java.util.Iterator;

import net.imglib2.ops.img.UnaryObjectFactory;
import net.imglib2.ops.operation.UnaryOutputOperation;
import net.imglib2.type.numeric.RealType;

/**
 * 
 * @author Felix Schoenenberger (University of Konstanz)
 * 
 * @param <T>
 */
public final class MakeHistogram< T extends RealType< T >> implements UnaryOutputOperation< Iterable< T >, OpsHistogram >
{

	int m_numBins = 0;

	public MakeHistogram()
	{
		this( -1 );
	}

	public MakeHistogram( int numBins )
	{
		m_numBins = numBins;
	}

	@Override
	public final UnaryObjectFactory< Iterable< T >, OpsHistogram > bufferFactory()
	{
		return new UnaryObjectFactory< Iterable< T >, OpsHistogram >()
		{
			@Override
			public OpsHistogram instantiate( Iterable< T > a )
			{
				return m_numBins <= 0 ? new OpsHistogram( a.iterator().next().createVariable() ) : new OpsHistogram( m_numBins, a.iterator().next().createVariable() );
			}
		};
	}

	@Override
	public final OpsHistogram compute( Iterable< T > op, OpsHistogram r )
	{
		final Iterator< T > it = op.iterator();
		r.clear();
		while ( it.hasNext() )
		{
			r.incByValue( it.next().getRealDouble() );
		}

		return r;
	}

	@Override
	public UnaryOutputOperation< Iterable< T >, OpsHistogram > copy()
	{
		return new MakeHistogram< T >( m_numBins );
	}
}