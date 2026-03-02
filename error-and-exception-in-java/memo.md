# Chap1
## Javaの思想
- 不正な形式のコードは動かない
    - badly formed code will not be run.
- 公式なエラーを返す唯一の手段は exceptions
- コンパイラはこれに対応する処理の実装を強制する

## 例外とは
- exceptional conditionとは
    - 現在の処理で、コードが正常な動作を行えなくなる状態

## Javaではどう書くか

```java
if (t == null)
    throw new NullPointerException();
```
- ヒープ上に new を使って生成される
- 現在の実行パスを停止し、例外ハンドラに処理を委譲
- exeption classの名前は何がうまくいかなかったかを表す