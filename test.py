def check(p):
	l = range(0,101,25)
	for i in range(len(l)-1):
		if p >= l[i] and p < l[i+1]:
			return
	print p
