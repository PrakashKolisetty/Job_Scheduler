"use client";
import Link from "next/link";
import { usePathname } from "next/navigation";
import styles from "./Navbar.module.css";

const Navbar = () => {
  const pathname = usePathname();

  return (
    <nav className={styles.navbar}>
      <div className={styles.logo}>Job Scheduler</div>
      <ul className={styles.navLinks}>
        <li>
          <Link href="/" className={pathname === "/" ? styles.active : ""}>
            Home
          </Link>
        </li>
        <li>
          <Link
            href="/jobs"
            className={pathname.startsWith("/jobs") ? styles.active : ""}
          >
            Jobs
          </Link>
        </li>
        <li>
          <Link
            href="/jobs/create"
            className={pathname === "/jobs/create" ? styles.active : ""}
          >
            Create Job
          </Link>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
