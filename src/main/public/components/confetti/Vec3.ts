export default class Vec3 {
    constructor(
        public x = 0,
        public y = 0,
        public z = 0,
    ){}
    random(scaleX = 1, scaleY=scaleX, scaleZ=scaleX){
        this.x = (Math.random()-0.5) * scaleX;
        this.y = (Math.random()-0.5) * scaleY;
        this.z = (Math.random()-0.5) * scaleZ;
    }
    scale(scale: number){
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
    }
    add(other: Vec3){
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }
    addMult(other: Vec3, mult: number){
        this.x += other.x * mult;
        this.y += other.y * mult;
        this.z += other.z * mult;
    }
    sub(other: Vec3){
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }
}
