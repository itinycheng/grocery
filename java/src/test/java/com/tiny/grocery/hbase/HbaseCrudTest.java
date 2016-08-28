package com.tiny.grocery.hbase;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

public class HbaseCrudTest {
	
	private Configuration conf = null;
	private Connection conn = null;
	
	@Before
	public void init() throws Exception {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "node01");
		conn = ConnectionFactory.createConnection(conf);
	}

	@Test
	public void testCreateTable() throws Exception {
		Admin admin = conn.getAdmin();
		TableName tname = TableName.valueOf("teacher");
		HTableDescriptor td = new HTableDescriptor(tname);
		HColumnDescriptor infoCd = new HColumnDescriptor("info");
		infoCd.setMaxVersions(10);
		HColumnDescriptor metaCd = new HColumnDescriptor("meta");
		metaCd.setMaxVersions(10);
		td.addFamily(infoCd);
		td.addFamily(metaCd);
		admin.createTable(td);
		admin.close();
	}
	
	@Test
	public void testDropTable() throws Exception {
		Admin admin = conn.getAdmin();
		TableName tname = TableName.valueOf("student");
		admin.disableTable(tname);
		admin.deleteTable(tname);
		admin.close();
	}
	
	@Test
	public void testPut() throws Exception {
		Table ht = conn.getTable(TableName.valueOf("teacher"));
		Put put = new Put(Bytes.toBytes("rk002"));
		put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("hanmeimei"));
		put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(19));
		put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("like"), Bytes.toBytes("book"));
		put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("dream"), Bytes.toBytes("writer"));
		ht.put(put);
		ht.close();
	}
	
	@Test
	public void testPutList() throws Exception {
		Table ht = conn.getTable(TableName.valueOf("teacher"));
		List<Put> list = new ArrayList<Put>(5000);
		for (int i = 0; i < 500000; i++) {
			Put put = new Put(Bytes.toBytes("rk00" + i));
			put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("hanmeimei" + i));
			put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(19));
			put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("like"), Bytes.toBytes("book" + i));
			put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("dream"), Bytes.toBytes("writer" + i));
			list.add(put);
			if(i % 5000 == 0){
				ht.put(list);
				list = new ArrayList<Put>(5000);
			}
		}
		if(!list.isEmpty()){
			ht.put(list);
		}
		ht.close();
	}
	
	@Test
	public void testUpdate() throws Exception {
		Table table = conn.getTable(TableName.valueOf("teacher"));
		Put put = new Put(Bytes.toBytes("rk001"));
		put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("dream"), Bytes.toBytes("any"));
		table.put(put);
		table.close();
	}
	
	/**
	 * 通过rowkey查询
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		Get get = new Get(Bytes.toBytes("rk002"));
		Table table = conn.getTable(TableName.valueOf("teacher"));
		Result result = table.get(get);
		for (Cell cell : result.listCells()) {
			if("age".equals(Bytes.toString(CellUtil.cloneQualifier(cell))))
				System.out.println(
						Bytes.toInt(CellUtil.cloneValue(cell)) + " : " +
						Bytes.toString(CellUtil.cloneQualifier(cell))
				);
			else 
				System.out.println(
						Bytes.toString(CellUtil.cloneValue(cell)) + " : " +
						Bytes.toString(CellUtil.cloneQualifier(cell))
				);
		} 
	}
	
	@Test
	public void testGetMultVer() throws Exception {
		Table table = conn.getTable(TableName.valueOf("teacher"));
		Get get = new Get(Bytes.toBytes("rk001"));
		get.setMaxVersions(10);
		get.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("dream"));
		Result result = table.get(get);
		for (Cell cell : result.listCells()) {
			System.out.println(
					Bytes.toString(CellUtil.cloneValue(cell)) + " : " +
					Bytes.toString(CellUtil.cloneQualifier(cell))
			);
		}
	}
	
	/**
	 * 全表扫描
	 * @throws Exception
	 */
	@Test
	public void testScan() throws Exception {
		Scan scan = new Scan();
		Table table = conn.getTable(TableName.valueOf("teacher"));
		ResultScanner resultScanner = table.getScanner(scan);
		for (Result result : resultScanner) {
			for (Cell cell : result.listCells()) {
				if("age".equals(Bytes.toString(CellUtil.cloneQualifier(cell))))
					System.out.println(
							Bytes.toInt(CellUtil.cloneValue(cell)) + " : " +
							Bytes.toString(CellUtil.cloneQualifier(cell))
					);
				else 
					System.out.println(
							Bytes.toString(CellUtil.cloneValue(cell)) + " : " +
							Bytes.toString(CellUtil.cloneQualifier(cell))
					);
			}
		}
		resultScanner.close();
	}
	
	@Test
	public void testDeleteCol() throws Exception {
		Table table = conn.getTable(TableName.valueOf("teacher"));
		Delete del = new Delete(Bytes.toBytes("rk001"));
		table.delete(del);
		table.close();
	}
	
	/**
	 * 测试 行级别的原子性
	 * @throws Exception
	 */
	@Test
	public void testPut2() throws Exception {
		Table ht = conn.getTable(TableName.valueOf("teacher"));
		
		List<Put> puts = new ArrayList<Put>();
		Put put1 = new Put(Bytes.toBytes("rk001"));
		put1.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("hanmeimei"));
		put1.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(19));
		put1.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("like"), Bytes.toBytes("book"));
		put1.addColumn(Bytes.toBytes("meta2"), Bytes.toBytes("dream"), Bytes.toBytes("writer"));
		puts.add(put1);
		Put put2 = new Put(Bytes.toBytes("rk002"));
		put2.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("hanmeimei4"));
		put2.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(194));
		put2.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("like"), Bytes.toBytes("book4"));
		put2.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("dream"), Bytes.toBytes("writer4"));
		puts.add(put2);
		ht.put(puts);
		ht.close();
	}
	
}
