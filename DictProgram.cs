using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices;
using System.Threading;

namespace Benzolamps
{
    /// <summary>
    /// 程序入口
    /// </summary>
    internal class DictProgram
    {
        private static void Main(string[] args)
        {
            try
            {
                string parameter = null;
                Console.BackgroundColor = ConsoleColor.Cyan;
                Console.ForegroundColor = ConsoleColor.Black;
                Console.Clear();
                HandleProcess(@".\jre\bin\javaw.exe", @"index tmp/dict/", false, false, (@in, err) => parameter = @in);
                HandleProcess(@".\mysql\bin\mysqld.exe", @"--defaults-file=.\mysql\my.ini --port=3306", false, true);
                HandleProcess(@".\jre\bin\java.exe", @"-jar dict.jar " + parameter, true, false, null);  
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        private static void HandleProcess(string path, string arguments = "", bool showWindow = false, bool async = false, Action<string, string> action = null)
        {
            Process process = new Process();
            process.StartInfo.FileName = path;
            process.StartInfo.Arguments = arguments;
            process.StartInfo.UseShellExecute = false;
            process.StartInfo.CreateNoWindow = !showWindow;

            if (!async)
            {
                if (action != null)
                {
                    process.StartInfo.RedirectStandardOutput = true;
                    process.StartInfo.RedirectStandardError = true;
                }
            }

            process.Start();

            if (!async)
            { 
                process.WaitForExit();
             
                if (action != null)
                {
                    string @in = process.StandardOutput.ReadToEnd();
                    string err = process.StandardError.ReadToEnd();
                    action(@in, err);
                }
            }
        }
    }
}
