package utils.benchmarks;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * This class provides two utility functions for locking 
 * files shared among multiple JVMs.
 */
public class LockUtils
{
	private FileLock lock = null;
	private FileChannel lockChannel = null;
	private File lockFile;

	/**
	 * This object creates an exclusive read-write lock on a file.
	 * It blocks until it can retrieve the lock.
	 * 
	 * @param file the file to lock.
	 * @return
	 */
	public LockUtils(File file)
	{
		String lockFileName = file.getName() + ".lock"; //$NON-NLS-1$
		File parent = file.getParentFile();
		if (parent == null)
			lockFile = new File(lockFileName);
		else
			lockFile = new File(parent, lockFileName);
	}	

	/**
	 * Obtains a lock on the file.  This blocks until the lock is obtained.
	 */
	public void lock()
	{
		// Get a file channel for the file
		try
		{
			lockFile.createNewFile();
			lockChannel = new RandomAccessFile(lockFile, "rw").getChannel(); //$NON-NLS-1$
		}
		catch (Exception e) {e.printStackTrace();}

		// This method blocks until it can retrieve the lock
		try
		{
			lock = lockChannel.lock();
			
		}
		catch (Exception e) {}
	}

	/**
	 * Releases the lock on the file.
	 */
	public void release()
	{
		try
		{
			// release the lock
			if (lock != null)
				lock.release();
			// close the channel
			if (lockChannel != null)
				lockChannel.close();
			// delete the lock file
			if (lockFile != null)
				lockFile.delete();
		}
		catch (IOException e) {}
	}
}
