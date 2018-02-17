#include "javacec2013_JNIfgeneric2013.h"
#include "Header.h"

/*
 * Class:     javacec2013_JNIfgeneric2013
 * Method:    initCEC2013
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_javacec2013_JNIfgeneric2013_initCEC2013(JNIEnv *env, jobject obj, jint funcID, jint dim)
{
	Benchmarks *fp;
	// run each of specified function in "configure.ini"
	if (funcID==1){
		fp = new F1();
	}else if (funcID==2){
		fp = new F2();
	}else if (funcID==3){
		fp = new F3();
	}else if (funcID==4){
		fp = new F4();
	}else if (funcID==5){
		fp = new F5();
	}else if (funcID==6){
		fp = new F6();
	}else if (funcID==7){
		fp = new F7();
	}else if (funcID==8){
		fp = new F8();
	}else if (funcID==9){
		fp = new F9();
	}else if (funcID==10){
		fp = new F10();
	}else if (funcID==11){
		fp = new F11();
	}else if (funcID==12){
		fp = new F12();
	}else if (funcID==13){
		fp = new F13();
	}else if (funcID==14){
		fp = new F14();
	}else if (funcID==15){
		fp = new F15();
	}else{
		exit(-1);
	}

	if (funcID==13 || funcID==14)
		fp->setDimension(905); // because of overlapping
	else
		fp->setDimension(dim);

	return reinterpret_cast<jlong>(fp);
}

/*
 * Class:     javacec2013_JNIfgeneric2013
 * Method:    exitCEC2013
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_javacec2013_JNIfgeneric2013_exitCEC2013(JNIEnv *env, jobject obj, jlong fpinput)
{
	Benchmarks *fp = reinterpret_cast<Benchmarks*>(fpinput);
	if(fp) {
		delete fp;
	}
}

/*
 * Class:     javacec2013_JNIfgeneric2013
 * Method:    evaluate
 * Signature: (J[D)D
 */
JNIEXPORT jdouble JNICALL Java_javacec2013_JNIfgeneric2013_evaluate(JNIEnv *env, jobject obj, jlong fp, jdoubleArray x)
{
	//jsize len = (*env)->GetArrayLength(env, x);
	jdouble *body = env->GetDoubleArrayElements(x, 0);
	jdouble retValue = reinterpret_cast<Benchmarks*>(fp)->compute(body);
	env->ReleaseDoubleArrayElements(x, body, 0); //JNI_ABORT, JNI_COMMIT
	return retValue;
}
