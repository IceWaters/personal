import java.util.*;

//����ɡ����й涨������
public class Test{
	private ArrayList<People> peoples = new ArrayList<People>();
	
	public Test(){		
		People p = new People("����",80,90,90);
		peoples.add(p);
		p = new People("�ŷ�",70,90,80);
		peoples.add(p);
		p = new People("����",85,95,90);
		peoples.add(p);
		p = new People("����",85,80,70);
		peoples.add(p);
		p = new People("����",85,80,75);
		peoples.add(p);		
	}
	
	//���ǻ�+����+�����ܺͽ������򣬽����д��people�����У�������һ��1��˳������
	public void setRank(){
		//����ʵ����ʵ�ַ���
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
	
	//����people�е�rankֵ��peoples��������
	//��������Ҫʹ��ArrayList����ط������������JDK API����
	public void ranking(){
		//����ʵ����ʵ�ַ���
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
			//System.out.println("����������"+p.getRank()+"����"+p.getName()+" �ǻ�("+p.getZhihui()+"),����("+p.getGongji()+"),����("+p.getFangyu()+")");
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
	
	//����佫�嵥
	public void listRank(){
		Iterator<People> it = peoples.iterator();
		while(it.hasNext()){
			People p = it.next();
			System.out.println("����������"+p.getRank()+"����"+p.getName()+" �ǻ�("+p.getZhihui()+"),����("+p.getGongji()+"),����("+p.getFangyu()+")");
		}
	}
	
	public static void main(String[] args){
		Test t = new Test();
		t.setRank();
		t.listRank();
		System.out.println("������ʾ-----------------------------");
		t.ranking();
		t.listRank();
	}
}