const NavBar: React.FC = () => {
  return (
    <div className="z-10 flex h-[60px] items-center justify-between bg-zinc-50 px-3 shadow-lg">
      <div className="flex items-center">
        <ul className="flex items-center">
          <li className="mr-6">
            <a className="text-xl text-zinc-950" href="/">
              Content Droid
            </a>
          </li>
          <li className="mr-6">
            <a className="text-zinc-500" href="/">
              Video Automation
            </a>
          </li>
        </ul>
      </div>
      <div>
        <ul>
          <li className="">
            <a className="text-zinc-500" href="/account">
              My Account
            </a>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default NavBar;
