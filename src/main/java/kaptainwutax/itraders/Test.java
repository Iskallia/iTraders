package kaptainwutax.itraders;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class Test {

	public static void main(String[] args) {
		List<Integer> integers = new ArrayList<>();
		integers.add(1);
		integers.add(2);
		integers.add(null);
		integers.add(4);
		integers.add(null);
		integers.add(null);
		integers.add(null);
		Lists.newArrayList(Iterators.limit(integers.iterator(), 5)).forEach(System.out::println);
	}
	
}
