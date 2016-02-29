import java.util.*;

//请完成★★★中规定的内容
public class Test{
	private ArrayList<People> peoples = new ArrayList<People>();
	
	public Test(){		
		People p = new People("关羽",80,90,90);
		peoples.add(p);
		p = new People("张飞",70,90,80);
		peoples.add(p);
		p = new People("赵云",85,95,90);
		peoples.add(p);
		p = new People("黄忠",85,80,70);
		peoples.add(p);
		p = new People("刘备",85,80,75);
		peoples.add(p);		
	}
	
	//按智慧+攻击+防御总和进行排序，将序号写入people对象中，排名第一是1，顺序增加
	public void setRank(){
		//★★★实验者实现方法
		Iterator<People> ite = peoples.iterator();
		Iterator<People> iter = peoples.iterator();
		float[] Rank=new float[5];
		int i=0;
		while(ite.hasNext()){
			People pe = ite.next();
			Rank[i]=pe.getZhihui()+pe.getGongji()+pe.getFangyu();
			i++;
		}
		int m,k;
		for(int j=0;j<5;j++){
			People peo = iter.next();
			m=0;
			for(k=0;k<5;k++){
				if(Rank[j]<Rank[k]){
					m++;
			}	
			peo.setRank(m+1);
		}
		}
		
	}
	
	//按照people中的rank值对peoples进行排序
	//该排序需要使用ArrayList的相关方法，具体查阅JDK API帮助
	public void ranking(){
		//★★★实验者实现方法
		ArrayList<People> peopless = new ArrayList<People>();
		Iterator<People> ir = peoples.iterator();
		while(ir.hasNext()){
			People pg = ir.next();
			peopless.add(pg);
		}
		peoples.clear();
		//Iterator<People> irf = peopless.iterator();
		for(int i=1;i<6;i++){
			Iterator<People> irf = peopless.iterator();
		while(irf.hasNext()){
			People p = irf.next();
			//System.out.println("三国排名【"+p.getRank()+"】："+p.getName()+" 智慧("+p.getZhihui()+"),攻击("+p.getGongji()+"),防御("+p.getFangyu()+")");
			if(p.getRank()==i){
				peoples.add(p);
				//peopless.remove
			}
		}
		}
		/*while(irf.hasNext()){
			People pgl = irf.next();
			peoples.add(pgl);
		*/
		
	}
	
	//输出武将清单
	public void listRank(){
		Iterator<People> it = peoples.iterator();
		while(it.hasNext()){
			People p = it.next();
			System.out.println("三国排名【"+p.getRank()+"】："+p.getName()+" 智慧("+p.getZhihui()+"),攻击("+p.getGongji()+"),防御("+p.getFangyu()+")");
		}
	}
	
	public static void main(String[] args){
		Test t = new Test();
		t.setRank();
		t.listRank();
		System.out.println("排序显示-----------------------------");
		t.ranking();
		t.listRank();
	}
}