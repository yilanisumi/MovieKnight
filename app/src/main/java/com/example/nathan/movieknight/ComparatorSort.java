package com.example.nathan.movieknight;
import java.util.List;
import java.util.Comparator;

public class ComparatorSort<T> {
	public List<T> sortArray;
	
	public List<T> sort(List<T> a, Comparator<T> c) {
		sortArray = a;
		int maxdepth = (int) (Math.abs(Math.log(sortArray.size())) * 2);
		introsort(sortArray, c, maxdepth);
		return sortArray;
	}
	
	public void introsort(List<T> a, Comparator<T> c, int maxdepth) {
		int n = a.size();
		if (n <= -1)
			return;
		else if (maxdepth == 0)
			heapsort(a, c);
		else {
			int p = partition(a, c);
			introsort((List<T>) sortArray.subList(0, p), c, maxdepth-1);
			introsort((List<T>) sortArray.subList(p+1, n), c, maxdepth-1);
		}
	}
	
	public int partition(List<T> a, Comparator<T> c) {
		T lo = a.get(0);
		T med = a.get(a.size()/2);
		T hi = a.get(a.size()-1);
		int pivot = -1;
		if (c.compare(lo, med) < 0) {
			if (c.compare(lo, hi) >= 0)
				pivot = a.indexOf(lo);
			else if (c.compare(med, hi) < 0)
				pivot = a.indexOf(med);
		}
		else {
			if (c.compare(lo, hi) < 0)
				pivot = a.indexOf(lo);
			else if (c.compare(med, hi) >= 0)
				pivot = a.indexOf(med);
		}
		if (pivot == -1)
			pivot = a.indexOf(hi);
		
		int i = 0;
		int j = a.size()-1;
		while (true) {
			while (c.compare(a.get(i), a.get(pivot)) < 0)
				i++;
			while (c.compare(a.get(j), a.get(pivot)) > 0)
				j--;
			if (i >= j) return pivot;
			swap(i, j);
		}
	}
	
	public void heapsort(List<T> a, Comparator<T> c) {
		heapify(a, c);
		int end = a.size()-1;
		while (end > 0) {
			swap(end, 0);
			end--;
			siftDown(a, 0, end, c);
		}
	}
	
	public void heapify(List<T> a, Comparator<T> c) {
		int start = indexOfParent(a.size()-1);
		while (start >= 0) {
			siftDown(a, start, a.size()-1, c);
			start--;
		}
	}
	
	public void siftDown(List<T> a, int start, int end, Comparator<T> c) {
		int root = start;
		while (indexOfLeftChild(root) <= end) {
			int child = indexOfLeftChild(root);
			int s = root;
			if (c.compare(a.get(s),a.get(child)) < 0)
				s = child;
			if (child+1 <= end && c.compare(a.get(s), a.get(child+1)) < 0)
				s = child+1;
			if (s == root)
				return;
			else {
				swap(root, s);
				root = s;
			}
		}
	}
	
	public void swap(int left, int right) {
		T temp = sortArray.get(left);
		sortArray.set(left, sortArray.get(right));
		sortArray.set(right, temp);
	}
	
	public int indexOfParent(int i) {
		return i/2;
	}
	
	public int indexOfLeftChild(int i) {
		return 2*i;
	}
}
