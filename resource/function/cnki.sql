SELECT
	id,
	titleCn,
	titleEn,
	absEn
FROM
	cnki_papers
WHERE
	titleCn LIKE '基于%遗传算法%的%'
OR titleCn LIKE '基于%支持向量机%的%'
OR titleCn LIKE '基于%SVM%的%'
OR titleCn LIKE '基于%神经网络%的%'
OR titleCn LIKE '基于%条件随机场%的%'
OR titleCn LIKE '基于%最大熵模型%的%'
OR titleCn LIKE '基于%小波变换%的%'
OR titleCn LIKE '基于%CRF%的%'
OR titleCn LIKE '基于%LDA%的%'
OR titleCn LIKE '基于%主题模型%的%'
OR titleCn LIKE '基于%隐马尔可夫模型%的%'
OR titleCn LIKE '基于粗糙集的%'