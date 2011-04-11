package tests;

import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ColorProcessor;
import mpicbg.imglib.RandomAccessibleInterval;
import mpicbg.imglib.display.ARGBScreenImage;
import mpicbg.imglib.display.RealARGBConverter;
import mpicbg.imglib.display.XYProjector;
import mpicbg.imglib.img.Img;
import mpicbg.imglib.img.ImgFactory;
import mpicbg.imglib.img.array.ArrayImgFactory;
import mpicbg.imglib.io.ImgOpener;
import mpicbg.imglib.type.numeric.ARGBType;
import mpicbg.imglib.type.numeric.real.FloatType;
import mpicbg.imglib.view.Views;

public class OpenAndDisplayRotated
{	
	final static public void main( final String[] args )
	{
		new ImageJ();
		
		Img< FloatType > img = null;
		try
		{
			ImgFactory< FloatType > imgFactory = new ArrayImgFactory< FloatType >();
			final ImgOpener io = new ImgOpener();
			img = io.openImg( "/home/tobias/Desktop/73.tif", imgFactory, new FloatType() );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return;
		}
		
		RandomAccessibleInterval< FloatType > view = Views.rotatedView( Views.hyperSlice( img, 2, 0 ), 0, 1 );

		final ARGBScreenImage screenImage = new ARGBScreenImage( ( int )view.dimension( 0 ), ( int )view.dimension( 1 ) );
		final XYProjector< FloatType, ARGBType > projector = new XYProjector< FloatType, ARGBType >( view, screenImage, new RealARGBConverter< FloatType >( 0, 127 ) );
		
		final ColorProcessor cp = new ColorProcessor( screenImage.image() );
		final ImagePlus imp = new ImagePlus( "argbScreenProjection", cp );
		imp.show();

		for ( int k = 0; k < 3; ++k ) 
			for ( int i = 0; i < view.dimension( 2 ); ++i )
			{
				projector.setPosition( i, 2 );
				projector.map();
				final ColorProcessor cpa = new ColorProcessor( screenImage.image() );
				imp.setProcessor( cpa );
				imp.updateAndDraw();
			}
		
		projector.map();
		
		projector.setPosition( 40, 2 );
		projector.map();
	}
}
