import java.util.*;

public class MCMapGen{
    
    private int LX, LY, LZ;
    private double PER;
    private MCGrid g;
    private MCWaterGrid trans;
    
    public MCMapGen(int lx, int ly, int lz){
        LX = lx;
        LY = ly;
        LZ = lz;
    }
    
    public MCGrid getGrid(){
        return g;
    }
    
    public MCWaterGrid getWater(){
        return trans;
    }
    
    public void genMap(){
        
		g = new MCGrid(LX, LY, LZ);
		trans = new MCWaterGrid(LX, LY, LZ);
        
        int[][] height = new int[LX][LY];
		double HEIGHT = LX/10.;
        
        double[][] m = perlinNoise(randGrid(LX, LY), (int)(6+2*Math.random()), 0.4+0.15*Math.random());
        double[][] m2 = perlinNoise(randGrid(LX, LY), (int)(14+3*Math.random()), 0.3+0.15*Math.random());
        
        int minh = Integer.MAX_VALUE, maxh = Integer.MIN_VALUE, toth = 0;
        
        for(int i = 0; i<LX; i++)
            for(int j = 0; j<LY; j++){
                height[i][j] = (int)((m[i][j]+m2[i][j])/2*LZ);
                minh = Math.min(minh, height[i][j]);
                maxh = Math.max(maxh, height[i][j]);
                toth += height[i][j];
            }
        
        int G = ((toth/(LX*LY)-minh)*2/3+minh);
        
		//grass and ground
		for(int x = 0; x<LX; x++)
            for(int y = 0; y<LY; y++)
            {
                
                if( height[x][y] > G +2){
                    g.set(x, y, height[x][y], loadBlock('g',x,y,height[x][y]));
                    for(int z = height[x][y]-1; z>=0; z--){
                        char block = (height[x][y] - z)/10.0 < Math.random()?'d':'t';
                        g.set(x, y, z, loadBlock(block, x,y,z));
                    }
                }else{
                    for(int z = height[x][y]; z>=0 && z > height[x][y]-10; z--)
                        g.set(x, y, z, loadBlock('s', x,y,z));
                    for(int z = height[x][y]-10; z>=0; z--){
                        char block = (height[x][y] - z)/10.0 < Math.random()?'d':'t';
                        g.set(x, y, z, loadBlock(block, x,y,z));
                    }
                }
            }
        
		//add water
		for(int x = 0; x<LX; x++)
            for(int y = 0; y<LY; y++)
            {
                for(int z = G; z>height[x][y]; z--)
                    trans.set(x, y, z, loadBlock('w', x,y,z));
            }
        
        boolean[][][] cave = cave(LX, LY, LZ, minh, maxh, toth/7);
        for(int a = 0; a<LX; a++)
            for(int b = 0; b<LY; b++)
                for(int c =0 ; c<LZ; c++)
                    if( cave[a][b][c]){
                        g.set(a, b, c,null);// loadBlock('d', a,b,c));
                    }
        
		//trans.set(LX/2, LY/2, LZ-20 ,loadBlock('w', LX/2, LY/2, LZ-20));

    }
    
    
    boolean[][][] cave(int LX, int LY, int LZ, int minh, int maxh, int totc){
        
        boolean[][][] m = new boolean[LX][LY][LZ];
        boolean[][][] vis = new boolean[LX][LY][LZ];
        int[][][] ct = new int[LX][LY][LZ];
        
        int diff = maxh-minh;
        int tot = 0;
        int seeds = 0;
        final int DR = 2;
        double DDR = Math.pow(DR*2+1,3);
    outer:
        while(tot < totc){
            int x = (int)(LX*Math.random()),
            y = (int)(LY*Math.random()),
            z = (int)(maxh*Math.random());
            double r = 1+1*Math.random();
            double t = 0;
            seeds ++;
            
            while(tot < totc && r >0){
                {
                    r += 0.75*(Math.random()-0.5);
                    double trr = r*r;
                    int rr = (int)Math.ceil(r);
                    for(int a = x-rr; a<=x+rr; a++)
                        for(int b = y-rr; b<=y+rr; b++)
                            for(int c = z-rr; c<=z+rr; c++)
                                if( (a-x)*(a-x) + (b-y)*(b-y) + (c-z)*(c-z) < trr
                                   && a>=0 && b>=0 && a<LX && b<LY && c>=0 && c<LZ && !m[a][b][c]){
                                    m[a][b][c] = true;
                                    for(int aa = a-DR; aa<=a+DR; aa++)
                                        for(int bb = b-DR; bb<=b+DR; bb++)
                                            for(int cc = c-DR; cc<=c+DR; cc++)
                                                if(aa>=0 && bb>=0 && aa<LX && bb<LY && cc>=0 && cc<LZ){
                                                    ct[aa][bb][cc]++;
                                                }
                                    tot++;
                                }
                }
                {
                    ArrayList<double[]> prob = new ArrayList<double[]>();
                    double totprob = 0;
                outer2:for(int a = x-1; a<=x+1; a++)
                        for(int b = y-1; b<=y+1; b++)
                            for(int c = z-1; c<=z+1; c++)
                                if(a>=0 && b>=0 && a<LX && b<LY && c>=0 && c<LZ){
                                    double pp = (DDR-ct[a][b][c]) / DDR;
                                    pp*=pp;
                                    totprob += pp;
                                    prob.add(new double[]{a,b,c,pp});
                                }
                    double rand = Math.random(), cur = 0;
                    for(double[] dr : prob){
                        if( rand < cur + dr[3]/totprob){
                            x = (int)dr[0];
                            y = (int)dr[1];
                            z = (int)dr[2];
                            break;
                        }
                        cur += dr[3]/totprob;
                    }
                }
                if( vis[x][y][z] ) break;
                vis[x][y][z] = true;
            }
        }
        
        System.out.println("seeds " + seeds);
        
        /*
        boolean[][][] m = new boolean[LX][LY][LZ],
            vis = new boolean[LX][LY][LZ],
            use = new boolean[LX][LY][LZ],
            adj = new boolean[LX][LY][LZ];
        int seeds = (int)(15+15*Math.random());
        int diff = maxh-minh;
        for(int i = 0; i<seeds; i++){
            int x = (int)(LX*Math.random()),
                y = (int)(LY*Math.random()),
                z = (int)(minh*0.8*Math.random()+diff*0.2*Math.random());
            int r = (int)(2+5*Math.random());
            int rr = r*r;
            for(int a = x-r; a<=x+r; a++)
                for(int b = y-r; b<=y+r; b++)
                    for(int c = z-r; c<=z+r; c++)
                        if( (a-x)*(a-x) + (b-y)*(b-y) + (c-z)*(c-z) < rr
                            && a>=0 && b>=0 && a<LX && b<LY && c>=0 && c<LZ){
                            m[a][b][c] = true;
                        }
        }
        int[][][] ct = new int[LX][LY][LZ];
        boolean change = false;
        int tot = 0, created = 0;
        do{
            change = false;
            for(int i = 0; i<LX; i++)
            for(int j = 0; j<LY; j++)
            for(int k = 0; k<LZ; k++)
                if( !vis[i][j][k] && m[i][j][k]){
                    int[] drs = {1, RR};
                    for(int dr = 0 ; dr<drs.length; dr++){
                        int DR = drs[dr];
                    for(int ii = i-DR; ii<=i+DR; ii++)
                    for(int jj = j-DR; jj<=j+DR; jj++)
                    for(int kk = k-DR; kk<=k+DR; kk++)
                        if( ii >=0 && jj >=0 && kk >=0 &&
                           ii < LX && jj < LY && kk < LZ){
                            if( dr == 0 ){
                                if( (ii&jj&kk) == 0)
                                adj[ii][jj][kk] = true;
                            }
                            else ct[ii][jj][kk]++;
                        }
                        
                        //=(ii-i)*(ii-i)+(jj-j)*(jj-j)+(kk-k)*(kk-k);
                    }
                    vis[i][j][k] = true;
                }
            ArrayList<int[]> add = new ArrayList<int[]>();
            int max = 0;
            for(int i = 0; i<LX; i++)
            for(int j = 0; j<LY; j++)
            for(int k = 0; k<LZ; k++)
                if( !m[i][j][k] && ct[i][j][k] > 0 && adj[i][j][k]){
                    use[i][j][k] = true;
                    double x = (double)ct[i][j][k]/RRR;
                    max = Math.max(ct[i][j][k], max);
                    if( x < CONVLIM){
                        double y = 1-(1/(Math.pow((x-CONVLIM)*K1, 6)+1));
                        System.out.println(ct[i][j][k] + " "+ x + " " +y);
                        if( Math.random() < y){
                            change = true;
                            add.add(new int[]{i, j, k});
                            created ++;
                        }
                    }
                }
            for(int[] ar : add)
                m[ar[0]][ar[1]][ar[2]] = true;
            System.out.println(max + " " +created);
        }while(change && tot++<10);*/
        return m;
    }
    
    //http://devmag.org.za/2009/04/25/perlin-noise/
    
    double[][] smoothNoise(double[][] b, int o){
        int row = b.length;
        int col = b[0].length;
        
        double[][] m = new double[row][col];
        
        int p = 1 << o;
        double f = 1.0 / p;
        
        for (int i = 0; i < row; i++){
            int i0 = i >> o << o;
            int i1 = (i0 + p) % row;
            double hBlend = (i - i0) * f;
            for (int j = 0; j < col; j++){
                int j0 = j >> o << o;
                int j1 = (j0 + p) % col;
                double vBlend = (j - j0) * f;
                
                double top = interpolate(b[i0][j0],
                                        b[i1][j0], hBlend);
                
                double bottom = interpolate(b[i0][j1],
                                           b[i1][j1], hBlend);
                
                m[i][j] = interpolate(top, bottom, vBlend);
            }
        }
        
        return m;
    }
    
    double[][] perlinNoise(double[][] b, int o, double p){
        int row = b.length;
        int col = b[0].length;
        
        double[][][] sn = new double[o][][];
        
        for (int i = 0; i < o; i++)
            sn[i] = smoothNoise(b, i);
        
        double[][] pn = new double[row][col];
        double amp = 1;
        double tot = 0;
        
        for (int oo = o-1; oo >= 0; oo--){
            amp *= p;
            tot += amp;
            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++)
                    pn[i][j] += sn[oo][i][j] * amp;
        }
        
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                pn[i][j] /= tot;
        
        return pn;
    }
    
    double interpolate(double x0, double x1, double alpha){
        return x0 * (1 - alpha) + alpha * x1;
    }
    
    double[][] randGrid(int row, int col){
        double[][] m = new double[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                m[i][j] = Math.random();
        return m;
    }
    
    public static MCBlock loadBlock(char c, int x, int y, int z){
        return MineCraft.loadBlock(c, x, y, z);
    }
    
}
